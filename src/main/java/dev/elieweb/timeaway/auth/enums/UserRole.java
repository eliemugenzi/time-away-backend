package dev.elieweb.timeaway.auth.enums;

public enum UserRole {
    ROLE_ADMIN,
    ROLE_MANAGER,
    ROLE_USER;

    public String getRole() {
        return this.name();
    }

    public static UserRole fromString(String role) {
        try {
            return role != null ? UserRole.valueOf("ROLE_" + role.toUpperCase()) : UserRole.ROLE_USER;
        } catch (IllegalArgumentException e) {
            return UserRole.ROLE_USER;
        }
    }
} 