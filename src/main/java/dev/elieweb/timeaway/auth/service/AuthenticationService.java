package dev.elieweb.timeaway.auth.service;

import dev.elieweb.timeaway.auth.dto.AuthenticationRequest;
import dev.elieweb.timeaway.auth.dto.AuthenticationResponse;
import dev.elieweb.timeaway.auth.dto.RegisterRequest;
import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.repository.UserRepository;
import dev.elieweb.timeaway.auth.security.JwtService;
import dev.elieweb.timeaway.common.exception.ResourceNotFoundException;
import dev.elieweb.timeaway.common.exception.TokenRefreshException;
import dev.elieweb.timeaway.department.repository.DepartmentRepository;
import dev.elieweb.timeaway.job.repository.JobTitleRepository;
import dev.elieweb.timeaway.leave.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final JobTitleRepository jobTitleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final LeaveBalanceService leaveBalanceService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Get the department
        var department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        // Determine the role based on department
        UserRole role = request.getRole();
        if (role == null) {
            role = "Human Resources".equals(department.getName()) ? 
                   UserRole.ROLE_HR : UserRole.ROLE_USER;
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .department(department)
                .jobTitle(jobTitleRepository.findById(request.getJobTitleId())
                        .orElseThrow(() -> new ResourceNotFoundException("Job title not found")))
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        
        // Initialize leave balances for the new user
        leaveBalanceService.initializeLeaveBalance(savedUser);

        String jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    String accessToken = jwtService.generateToken(token.getUser());
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token not found in database"));
    }
} 