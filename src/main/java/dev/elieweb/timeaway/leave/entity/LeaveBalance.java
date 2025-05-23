package dev.elieweb.timeaway.leave.entity;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.common.entity.BaseEntity;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Column(name = "monthly_accrual_rate", precision = 5, scale = 2)
    private BigDecimal monthlyAccrualRate;

    @Column(name = "carried_forward_days")
    private Integer carriedForwardDays;

    @Column(name = "last_accrual_date")
    private LocalDate lastAccrualDate;

    public LeaveBalance() {}

    public LeaveBalance(User user, LeaveType type, Integer year, Integer totalDays, 
                       Integer usedDays, Integer remainingDays, BigDecimal monthlyAccrualRate,
                       Integer carriedForwardDays, LocalDate lastAccrualDate) {
        this.user = user;
        this.type = type;
        this.year = year;
        this.totalDays = totalDays;
        this.usedDays = usedDays;
        this.remainingDays = remainingDays;
        this.monthlyAccrualRate = monthlyAccrualRate;
        this.carriedForwardDays = carriedForwardDays;
        this.lastAccrualDate = lastAccrualDate;
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

    public BigDecimal getMonthlyAccrualRate() {
        return monthlyAccrualRate;
    }

    public void setMonthlyAccrualRate(BigDecimal monthlyAccrualRate) {
        this.monthlyAccrualRate = monthlyAccrualRate;
    }

    public Integer getCarriedForwardDays() {
        return carriedForwardDays;
    }

    public void setCarriedForwardDays(Integer carriedForwardDays) {
        this.carriedForwardDays = carriedForwardDays;
    }

    public LocalDate getLastAccrualDate() {
        return lastAccrualDate;
    }

    public void setLastAccrualDate(LocalDate lastAccrualDate) {
        this.lastAccrualDate = lastAccrualDate;
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
                ", monthlyAccrualRate=" + monthlyAccrualRate +
                ", carriedForwardDays=" + carriedForwardDays +
                ", lastAccrualDate=" + lastAccrualDate +
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
        private BigDecimal monthlyAccrualRate;
        private Integer carriedForwardDays;
        private LocalDate lastAccrualDate;

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

        public LeaveBalanceBuilder monthlyAccrualRate(BigDecimal monthlyAccrualRate) {
            this.monthlyAccrualRate = monthlyAccrualRate;
            return this;
        }

        public LeaveBalanceBuilder carriedForwardDays(Integer carriedForwardDays) {
            this.carriedForwardDays = carriedForwardDays;
            return this;
        }

        public LeaveBalanceBuilder lastAccrualDate(LocalDate lastAccrualDate) {
            this.lastAccrualDate = lastAccrualDate;
            return this;
        }

        public LeaveBalance build() {
            return new LeaveBalance(user, type, year, totalDays, usedDays, remainingDays,
                                  monthlyAccrualRate, carriedForwardDays, lastAccrualDate);
        }
    }
} 