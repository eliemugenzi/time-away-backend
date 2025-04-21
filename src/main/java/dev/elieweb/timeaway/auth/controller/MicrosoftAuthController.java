package dev.elieweb.timeaway.auth.controller;

import dev.elieweb.timeaway.auth.dto.AuthenticationResponse;
import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.repository.UserRepository;
import dev.elieweb.timeaway.auth.security.JwtService;
import dev.elieweb.timeaway.auth.service.RefreshTokenService;
import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.department.entity.Department;
import dev.elieweb.timeaway.department.repository.DepartmentRepository;
import dev.elieweb.timeaway.leave.service.LeaveBalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.Collections;
import java.util.HashMap;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth/microsoft")
@RequiredArgsConstructor
@Tag(name = "Microsoft Authentication", description = "Microsoft Authentication APIs")
public class MicrosoftAuthController {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final LeaveBalanceService leaveBalanceService;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RestTemplate restTemplate;

    private String convertToFormData(MultiValueMap<String, String> params) {
        return params.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue().get(0), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));
    }

    @GetMapping("/callback")
    @Operation(summary = "Handle Microsoft OAuth2 callback")
    @Transactional
    public ResponseEntity<ApiResponse> handleCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletRequest request) {
        
        log.info("Starting OAuth2 callback processing...");

        try {
            // Get the Microsoft OAuth2 client registration
            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("azure-ad");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            
            if (clientRegistration == null) {
                log.error("No client registration found for Azure AD");
                return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Authentication failed: no client registration found"));
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
                    return ResponseEntity.status(tokenResponse.getStatusCode())
                        .body(ApiResponse.error("Token exchange failed: " + tokenResponse.getStatusCode()));
                }

                Map<String, String> tokens = tokenResponse.getBody();
                if (tokens == null || !tokens.containsKey("access_token")) {
                    log.error("Invalid token response - missing access token");
                    return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid token response"));
                }

                String idToken = tokens.get("id_token");
                log.info("Successfully obtained tokens");

                // Parse the ID token (it's a JWT) to get user info
                String[] idTokenParts = idToken.split("\\.");
                if (idTokenParts.length != 3) {
                    log.error("Invalid ID token format");
                    return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Authentication failed: invalid ID token format"));
                }

                // Decode the payload (second part of the JWT)
                String payload = new String(java.util.Base64.getUrlDecoder().decode(idTokenParts[1]));
                Map<String, Object> idTokenClaims = gson.fromJson(payload, Map.class);

                String email = (String) idTokenClaims.get("email");
                String firstName = (String) idTokenClaims.get("given_name");
                String lastName = (String) idTokenClaims.get("family_name");

                if (email == null || email.isEmpty()) {
                    log.error("No email found in ID token claims");
                    return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Authentication failed: email not found"));
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
                
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken.getToken())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build();

                log.info("Authentication successful for user: {}", email);
                return ResponseEntity.ok(ApiResponse.success(
                    "Authentication successful",
                    authResponse
                ));
                
            } catch (HttpClientErrorException e) {
                log.error("Token exchange failed: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                return ResponseEntity.status(e.getStatusCode())
                        .body(ApiResponse.error("Token exchange failed: " + e.getMessage()));
            } catch (Exception e) {
                log.error("Unexpected error during token exchange: {}", e.getMessage());
                return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Unexpected error during token exchange: " + e.getMessage()));
            }
        } catch (Exception e) {
            log.error("Error during OAuth2 callback processing: {}", e.getMessage());
            return ResponseEntity.badRequest()
                .body(ApiResponse.error("Authentication failed: " + e.getMessage()));
        }
    }

    @GetMapping("/error")
    @Operation(summary = "Handle Microsoft OAuth2 authentication failure")
    public ResponseEntity<ApiResponse> handleError(
            @RequestParam(value = "error", required = false) String error,
            HttpServletRequest request) {
        log.error("OAuth2 authentication error: {}", error);
        String errorMessage = error != null ? error : "Authentication failed";
        return ResponseEntity.status(401)
            .body(ApiResponse.error(errorMessage));
    }
} 