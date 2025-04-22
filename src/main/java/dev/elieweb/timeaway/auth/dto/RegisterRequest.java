package dev.elieweb.timeaway.auth.dto;

import dev.elieweb.timeaway.auth.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

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
    private UserRole role = UserRole.ROLE_USER;

    // Default constructor
    public RegisterRequest() {
    }

    // All-args constructor
    public RegisterRequest(String firstName, String lastName, String email, String password, 
                         UUID departmentId, UUID jobTitleId, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.departmentId = departmentId;
        this.jobTitleId = jobTitleId;
        this.role = role != null ? role : UserRole.ROLE_USER;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(UUID jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role != null ? role : UserRole.ROLE_USER;
    }

    // Builder pattern
    public static RegisterRequestBuilder builder() {
        return new RegisterRequestBuilder();
    }

    public static class RegisterRequestBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private UUID departmentId;
        private UUID jobTitleId;
        private UserRole role = UserRole.ROLE_USER;

        RegisterRequestBuilder() {
        }

        public RegisterRequestBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public RegisterRequestBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public RegisterRequestBuilder email(String email) {
            this.email = email;
            return this;
        }

        public RegisterRequestBuilder password(String password) {
            this.password = password;
            return this;
        }

        public RegisterRequestBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public RegisterRequestBuilder jobTitleId(UUID jobTitleId) {
            this.jobTitleId = jobTitleId;
            return this;
        }

        public RegisterRequestBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public RegisterRequest build() {
            return new RegisterRequest(firstName, lastName, email, password, 
                                    departmentId, jobTitleId, role);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegisterRequest that = (RegisterRequest) o;

        if (!firstName.equals(that.firstName)) return false;
        if (!lastName.equals(that.lastName)) return false;
        if (!email.equals(that.email)) return false;
        if (!password.equals(that.password)) return false;
        if (!departmentId.equals(that.departmentId)) return false;
        if (!jobTitleId.equals(that.jobTitleId)) return false;
        return role == that.role;
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + departmentId.hashCode();
        result = 31 * result + jobTitleId.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "RegisterRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", departmentId=" + departmentId +
                ", jobTitleId=" + jobTitleId +
                ", role=" + role +
                '}';
    }
}