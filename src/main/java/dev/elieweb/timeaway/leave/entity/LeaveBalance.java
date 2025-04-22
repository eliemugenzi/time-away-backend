package dev.elieweb.timeaway.leave.entity;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.common.entity.BaseEntity;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "leave_balances")
public class LeaveBalance extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType type;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer totalDays;

    @Column(nullable = false)
    private Integer usedDays;

    @Column(nullable = false)
    private Integer remainingDays;

    public LeaveBalance() {}

    public LeaveBalance(User user, LeaveType type, Integer year, Integer totalDays, 
                       Integer usedDays, Integer remainingDays) {
        this.user = user;
        this.type = type;
        this.year = year;
        this.totalDays = totalDays;
        this.usedDays = usedDays;
        this.remainingDays = remainingDays;
    }

    public static LeaveBalanceBuilder builder() {
        return new LeaveBalanceBuilder();
    }

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

    public Integer getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(Integer usedDays) {
        this.usedDays = usedDays;
    }

    public Integer getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(Integer remainingDays) {
        this.remainingDays = remainingDays;
    }

    @Override
    public String toString() {
        return "LeaveBalance{" +
                "id=" + getId() +
                ", user=" + (user != null ? user.getEmail() : null) +
                ", type=" + type +
                ", year=" + year +
                ", totalDays=" + totalDays +
                ", usedDays=" + usedDays +
                ", remainingDays=" + remainingDays +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaveBalance that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(user, that.user) &&
               type == that.type &&
               Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, type, year);
    }

    public static class LeaveBalanceBuilder {
        private User user;
        private LeaveType type;
        private Integer year;
        private Integer totalDays;
        private Integer usedDays;
        private Integer remainingDays;

        LeaveBalanceBuilder() {}

        public LeaveBalanceBuilder user(User user) {
            this.user = user;
            return this;
        }

        public LeaveBalanceBuilder type(LeaveType type) {
            this.type = type;
            return this;
        }

        public LeaveBalanceBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public LeaveBalanceBuilder totalDays(Integer totalDays) {
            this.totalDays = totalDays;
            return this;
        }

        public LeaveBalanceBuilder usedDays(Integer usedDays) {
            this.usedDays = usedDays;
            return this;
        }

        public LeaveBalanceBuilder remainingDays(Integer remainingDays) {
            this.remainingDays = remainingDays;
            return this;
        }

        public LeaveBalance build() {
            return new LeaveBalance(user, type, year, totalDays, usedDays, remainingDays);
        }
    }
} 