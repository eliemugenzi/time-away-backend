package dev.elieweb.timeaway.auth.dto;

import dev.elieweb.timeaway.auth.enums.UserRole;

public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;

    public AuthenticationResponse() {}

    public AuthenticationResponse(String accessToken, String refreshToken, String tokenType,
                                String firstName, String lastName, String email, UserRole role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
    }

    public static AuthenticationResponseBuilder builder() {
        return new AuthenticationResponseBuilder();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                '}';
    }

    public static class AuthenticationResponseBuilder {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";
        private String firstName;
        private String lastName;
        private String email;
        private UserRole role;

        AuthenticationResponseBuilder() {}

        public AuthenticationResponseBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public AuthenticationResponseBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public AuthenticationResponseBuilder tokenType(String tokenType) {
            this.tokenType = tokenType;
            return this;
        }

        public AuthenticationResponseBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public AuthenticationResponseBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public AuthenticationResponseBuilder email(String email) {
            this.email = email;
            return this;
        }

        public AuthenticationResponseBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(accessToken, refreshToken, tokenType, firstName, lastName, email, role);
        }
    }
} 