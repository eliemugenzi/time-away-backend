package dev.elieweb.timeaway.auth.controller;

import dev.elieweb.timeaway.auth.dto.AuthenticationRequest;
import dev.elieweb.timeaway.auth.dto.AuthenticationResponse;
import dev.elieweb.timeaway.auth.dto.RegisterRequest;
import dev.elieweb.timeaway.auth.dto.TokenRefreshRequest;
import dev.elieweb.timeaway.auth.service.AuthenticationService;
import dev.elieweb.timeaway.auth.security.JwtService;
import dev.elieweb.timeaway.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management endpoints")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok(ApiResponse.success("User registered successfully", response));
    }

    @Operation(summary = "Authenticate a user")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success("User authenticated successfully", response));
    }

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        AuthenticationResponse response = authenticationService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    @GetMapping("/microsoft/success")
    @Operation(summary = "Handle successful Microsoft OAuth2 authentication")
    public ResponseEntity<AuthenticationResponse> handleOAuth2Success(
            @AuthenticationPrincipal OAuth2User principal) {
        // Generate JWT token for the authenticated user
        String token = jwtService.generateToken(principal.getName());
        String refreshToken = jwtService.generateRefreshToken(principal.getName());
        
        return ResponseEntity.ok(AuthenticationResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken)
                .build());
    }
} 