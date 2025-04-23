package dev.elieweb.timeaway.auth.controller;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.repository.UserRepository;
import dev.elieweb.timeaway.auth.security.JwtService;
import dev.elieweb.timeaway.auth.service.RefreshTokenService;
import dev.elieweb.timeaway.department.entity.Department;
import dev.elieweb.timeaway.department.repository.DepartmentRepository;
import dev.elieweb.timeaway.leave.service.LeaveBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/auth/microsoft")
@RequiredArgsConstructor
@Tag(name = "Microsoft Authentication", description = "Microsoft Authentication APIs")
public class MicrosoftAuthController {
    private static final Logger log = LoggerFactory.getLogger(MicrosoftAuthController.class);
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final LeaveBalanceService leaveBalanceService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RestTemplate restTemplate;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Operation(summary = "Handle Microsoft OAuth2 callback")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "302", description = "Redirect to frontend with authentication tokens"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request or missing parameters")
    })
    @GetMapping("/callback")
    @Transactional
    public void handleCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        
        log.info("Starting OAuth2 callback processing...");

        try {
            // Get the Microsoft OAuth2 client registration
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("azure-ad");
            
            if (clientRegistration == null) {
                redirectWithError(response, "Authentication failed: no client registration found");
                return;
            }

            // Exchange the authorization code for tokens
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            StringBuilder formData = new StringBuilder();
            formData.append("client_id=").append(URLEncoder.encode(clientRegistration.getClientId(), StandardCharsets.UTF_8))
                   .append("&client_secret=").append(URLEncoder.encode(clientRegistration.getClientSecret(), StandardCharsets.UTF_8))
                   .append("&redirect_uri=").append(URLEncoder.encode(clientRegistration.getRedirectUri(), StandardCharsets.UTF_8))
                   .append("&code=").append(URLEncoder.encode(code, StandardCharsets.UTF_8))
                   .append("&grant_type=authorization_code")
                   .append("&scope=").append(URLEncoder.encode(String.join(" ", clientRegistration.getScopes()), StandardCharsets.UTF_8));

            HttpEntity<String> requestEntity = new HttpEntity<>(formData.toString(), headers);

            try {
                log.info("Exchanging authorization code for tokens...");
                ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                    clientRegistration.getProviderDetails().getTokenUri(),
                    requestEntity,
                    Map.class
                );
                
                if (!tokenResponse.getStatusCode().is2xxSuccessful()) {
                    log.error("Token exchange failed with status: {}", tokenResponse.getStatusCode());
                    redirectWithError(response, "Token exchange failed: " + tokenResponse.getStatusCode());
                    return;
                }

                Map<String, Object> tokens = tokenResponse.getBody();
                if (tokens == null || !tokens.containsKey("access_token")) {
                    log.error("Invalid token response - missing access token");
                    redirectWithError(response, "Invalid token response");
                    return;
                }

                String idToken = (String) tokens.get("id_token");
                log.info("Successfully obtained tokens");

                // Parse the ID token (it's a JWT) to get user info
                String[] idTokenParts = idToken.split("\\.");
                if (idTokenParts.length != 3) {
                    log.error("Invalid ID token format");
                    redirectWithError(response, "Authentication failed: invalid ID token format");
                    return;
                }

                // Decode the payload (second part of the JWT)
                String payload = new String(java.util.Base64.getUrlDecoder().decode(idTokenParts[1]));
                Map<String, Object> idTokenClaims = gson.fromJson(payload, Map.class);

                String email = (String) idTokenClaims.get("email");
                
                // Get user's name from claims
                final String[] names = extractNames(idTokenClaims);
                final String firstName = names[0];
                final String lastName = names[1];

                if (email == null || email.isEmpty()) {
                    log.error("No email found in ID token claims");
                    redirectWithError(response, "Authentication failed: email not found");
                    return;
                }

                // Find or create user
                User user = userRepository.findByEmail(email)
                        .orElseGet(() -> {
                            log.info("Creating new user with email: {}", email);
                            
                            Department hrDepartment = departmentRepository.findByName("Human Resources")
                                    .orElse(null);

                            User newUser = User.builder()
                                    .email(email)
                                    .firstName(firstName)
                                    .lastName(lastName)
                                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                                    .role(hrDepartment != null ? UserRole.ROLE_HR : UserRole.ROLE_USER)
                                    .joiningDate(LocalDateTime.now())
                                    .department(hrDepartment)
                                    .build();

                            User savedUser = userRepository.save(newUser);
                            leaveBalanceService.initializeLeaveBalance(savedUser);
                            return savedUser;
                        });

                // Generate our own tokens
                String jwtToken = jwtService.generateToken(user);
                var refreshToken = refreshTokenService.createRefreshToken(user);
                

                log.info("Authentication successful for user: {}", email);
                
                // Redirect to frontend with success response
                String successUrl = UriComponentsBuilder.fromUriString(frontendUrl)
                    .path("/auth/callback")
                    .queryParam("token", jwtToken)
                    .queryParam("refreshToken", refreshToken.getToken())
                    .queryParam("email", user.getEmail())
                    .queryParam("firstName", user.getFirstName())
                    .queryParam("lastName", user.getLastName())
                    .queryParam("role", user.getRole())
                    .build()
                    .encode()
                    .toUriString();

                response.sendRedirect(successUrl);
                
            } catch (HttpClientErrorException e) {
                log.error("Token exchange failed: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                redirectWithError(response, "Token exchange failed: " + e.getMessage());
            } catch (Exception e) {
                log.error("Unexpected error during token exchange: {}", e.getMessage());
                redirectWithError(response, "Unexpected error during authentication");
            }
        } catch (Exception e) {
            log.error("Error during OAuth2 callback processing: {}", e.getMessage());
            redirectWithError(response, "Authentication failed: " + e.getMessage());
        }
    }

    private void redirectWithError(HttpServletResponse response, String error) throws IOException {
        String errorUrl = UriComponentsBuilder.fromUriString(frontendUrl)
            .path("/auth/callback")
            .queryParam("error", URLEncoder.encode(error, StandardCharsets.UTF_8))
            .build()
            .encode()
            .toUriString();
        
        response.sendRedirect(errorUrl);
    }

    @Operation(summary = "Handle Microsoft OAuth2 authentication failure")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "302", description = "Redirect to frontend with error message")
    })
    @GetMapping("/error")
    public void handleError(
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        log.error("OAuth2 authentication error: {}", error);
        String errorMessage = error != null ? error : "Authentication failed";
        redirectWithError(response, errorMessage);
    }

    private String[] extractNames(Map<String, Object> idTokenClaims) {
        String firstName = (String) idTokenClaims.get("given_name");
        String lastName = (String) idTokenClaims.get("family_name");

        // If given_name and family_name are not available, try to use name
        if ((firstName == null || lastName == null) && idTokenClaims.containsKey("name")) {
            String fullName = (String) idTokenClaims.get("name");
            String[] nameParts = fullName.split(" ", 2);
            firstName = nameParts[0];
            lastName = nameParts.length > 1 ? nameParts[1] : "";
            log.info("Using name field for first and last name: {} {}", firstName, lastName);
        }

        // If we still don't have valid names, use defaults
        if (firstName == null || firstName.trim().isEmpty()) {
            firstName = "---";
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            lastName = "---";
        }

        return new String[]{firstName, lastName};
    }
} 