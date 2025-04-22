package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LeaveBalanceDTO {
    @NotNull(message = "User is required")
    private User user;

    @NotNull(message = "Leave type is required")
    private LeaveType type;

    @NotNull(message = "Year is required")
    @Min(value = 2024, message = "Year must be 2024 or later")
    private Integer year;

    @NotNull(message = "Total days is required")
    @Min(value = 1, message = "Total days must be at least 1")
    private Integer totalDays;

    // Default constructor
    public LeaveBalanceDTO() {
    }

    // All-args constructor
    public LeaveBalanceDTO(User user, LeaveType type, Integer year, Integer totalDays) {
        this.user = user;
        this.type = type;
        this.year = year;
        this.totalDays = totalDays;
    }

    // Getters and setters
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LeaveType getType() {
        return type;
    }

    public void setType(LeaveType type) {
        this.type = type;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Integer totalDays) {
        this.totalDays = totalDays;
    }

    // Builder pattern
    public static LeaveBalanceDTOBuilder builder() {
        return new LeaveBalanceDTOBuilder();
    }

    public static class LeaveBalanceDTOBuilder {
        private User user;
        private LeaveType type;
        private Integer year;
        private Integer totalDays;

        LeaveBalanceDTOBuilder() {
        }

        public LeaveBalanceDTOBuilder user(User user) {
            this.user = user;
            return this;
        }

        public LeaveBalanceDTOBuilder type(LeaveType type) {
            this.type = type;
            return this;
        }

        public LeaveBalanceDTOBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public LeaveBalanceDTOBuilder totalDays(Integer totalDays) {
            this.totalDays = totalDays;
            return this;
        }

        public LeaveBalanceDTO build() {
            return new LeaveBalanceDTO(user, type, year, totalDays);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveBalanceDTO that = (LeaveBalanceDTO) o;

        if (!user.equals(that.user)) return false;
        if (type != that.type) return false;
        if (!year.equals(that.year)) return false;
        return totalDays.equals(that.totalDays);
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + year.hashCode();
        result = 31 * result + totalDays.hashCode();
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "LeaveBalanceDTO{" +
                "user=" + user +
                ", type=" + type +
                ", year=" + year +
                ", totalDays=" + totalDays +
                '}';
    }
} 