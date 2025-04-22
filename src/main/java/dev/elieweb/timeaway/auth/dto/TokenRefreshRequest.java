package dev.elieweb.timeaway.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class TokenRefreshRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;

    // Default constructor
    public TokenRefreshRequest() {
    }

    // All-args constructor
    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Getter and setter
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    // Builder pattern
    public static TokenRefreshRequestBuilder builder() {
        return new TokenRefreshRequestBuilder();
    }

    public static class TokenRefreshRequestBuilder {
        private String refreshToken;

        TokenRefreshRequestBuilder() {
        }

        public TokenRefreshRequestBuilder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokenRefreshRequest build() {
            return new TokenRefreshRequest(refreshToken);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenRefreshRequest that = (TokenRefreshRequest) o;

        return refreshToken.equals(that.refreshToken);
    }

    @Override
    public int hashCode() {
        return refreshToken.hashCode();
    }

    // toString
    @Override
    public String toString() {
        return "TokenRefreshRequest{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
} 