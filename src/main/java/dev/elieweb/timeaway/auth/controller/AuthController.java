package dev.elieweb.timeaway.auth.controller;

import dev.elieweb.timeaway.auth.dto.AuthenticationRequest;
import dev.elieweb.timeaway.auth.dto.AuthenticationResponse;
import dev.elieweb.timeaway.auth.dto.RegisterRequest;
import dev.elieweb.timeaway.auth.dto.TokenRefreshRequest;
import dev.elieweb.timeaway.auth.service.AuthenticationService;
import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.common.exception.TokenRefreshException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse> refreshToken(
            @Valid @RequestBody TokenRefreshRequest request
    ) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiResponse> handleTokenRefreshException(TokenRefreshException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
} 