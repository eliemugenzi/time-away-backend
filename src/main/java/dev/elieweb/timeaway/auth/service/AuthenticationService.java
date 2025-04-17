package dev.elieweb.timeaway.auth.service;

import dev.elieweb.timeaway.auth.dto.AuthenticationRequest;
import dev.elieweb.timeaway.auth.dto.AuthenticationResponse;
import dev.elieweb.timeaway.auth.dto.RegisterRequest;
import dev.elieweb.timeaway.auth.dto.TokenRefreshRequest;
import dev.elieweb.timeaway.auth.entity.RefreshToken;
import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.repository.UserRepository;
import dev.elieweb.timeaway.common.exception.TokenRefreshException;
import dev.elieweb.timeaway.auth.security.JwtService;
import dev.elieweb.timeaway.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public ApiResponse register(@Valid RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Set default role if not provided
        UserRole userRole = request.getRole();
        if (userRole == null) {
            userRole = UserRole.ROLE_USER;
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .department(request.getDepartment())
                .role(userRole)
                .build();

        user = userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        var response = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();

        return ApiResponse.success(response);
    }

    public ApiResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        var response = AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();

        return ApiResponse.success(response);
    }

    public ApiResponse refreshToken(TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.getRefreshToken())
                .map(refreshToken -> {
                    refreshTokenService.verifyExpiration(refreshToken);
                    var user = refreshToken.getUser();
                    var jwtToken = jwtService.generateToken(user);

                    var response = AuthenticationResponse.builder()
                            .accessToken(jwtToken)
                            .refreshToken(refreshToken.getToken())
                            .build();

                    return ApiResponse.success(response);
                })
                .orElseThrow(() -> new TokenRefreshException(request.getRefreshToken(), "Refresh token not found"));
    }
} 