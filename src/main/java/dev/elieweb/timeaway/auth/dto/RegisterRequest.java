package dev.elieweb.timeaway.auth.dto;

import dev.elieweb.timeaway.auth.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for user registration")
public class RegisterRequest {
    @Schema(description = "User's first name", example = "John")
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "User's last name", example = "Doe")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "User's password", example = "strongPassword123")
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @Schema(description = "User's department ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @Schema(description = "User's job title ID", example = "123e4567-e89b-12d3-a456-426614174001")
    @NotNull(message = "Job title ID is required")
    private UUID jobTitleId;

    @Schema(description = "User's role", example = "ROLE_USER", defaultValue = "ROLE_USER")
    @Builder.Default
    private UserRole role = UserRole.ROLE_USER;
}