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
import dev.elieweb.timeaway.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    
    private static final Set<String> HR_DEPARTMENT_NAMES = Set.of(
            "human resources",
            "hr",
            "human resource",
            "hr department",
            "human resources department"
    );

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final JobTitleRepository jobTitleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final LeaveBalanceService leaveBalanceService;
    private final EmailService emailService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Starting user registration process for email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email already exists - {}", request.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        // Get the department
        var department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> {
                    log.error("Registration failed: Department not found - ID: {}", request.getDepartmentId());
                    return new ResourceNotFoundException("Department not found");
                });

        // Determine the role based on department
        UserRole role = request.getRole();
        if (role == null) {
            String departmentNameLower = department.getName().toLowerCase().trim();
            role = HR_DEPARTMENT_NAMES.contains(departmentNameLower) ? 
                   UserRole.ROLE_HR : UserRole.ROLE_USER;
            log.debug("Assigned role {} based on department {}", role, department.getName());
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .department(department)
                .jobTitle(jobTitleRepository.findById(request.getJobTitleId())
                        .orElseThrow(() -> {
                            log.error("Registration failed: Job title not found - ID: {}", request.getJobTitleId());
                            return new ResourceNotFoundException("Job title not found");
                        }))
                .role(role)
                .joiningDate(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User successfully created: {} {}", savedUser.getFirstName(), savedUser.getLastName());
        
        // Initialize leave balances for the new user
        leaveBalanceService.initializeLeaveBalance(savedUser);
        log.info("Leave balances initialized for user: {}", savedUser.getEmail());

        // Send welcome email
        try {
            log.info("Attempting to send welcome email to: {}", savedUser.getEmail());
            emailService.sendWelcomeEmail(savedUser);
            log.info("Welcome email sent successfully to: {}", savedUser.getEmail());
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", savedUser.getEmail(), e.getMessage(), e);
            // Log the error but don't fail the registration
        }

        String jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = refreshTokenService.createRefreshToken(savedUser);
        log.info("Authentication tokens generated for user: {}", savedUser.getEmail());

        log.info("User registration completed successfully for: {}", savedUser.getEmail());
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .role(savedUser.getRole())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        System.out.println("Authentication successful: " + authentication.toString());

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken.getToken())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public AuthenticationResponse refreshToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(token -> {
                    User user = token.getUser();
                    String accessToken = jwtService.generateToken(user);
                    return AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .role(user.getRole())
                            .build();
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh token not found in database"));
    }
} 