package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class LeaveBalanceResponseDTO {
    private UUID id;
    private UUID userId;
    private String userName;
    private LeaveType type;
    private Integer year;
    private Integer totalDays;
    private Integer usedDays;
    private Integer remainingDays;
    private BigDecimal monthlyAccrualRate;
    private Integer carriedForwardDays;
    private LocalDate lastAccrualDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public LeaveBalanceResponseDTO() {
    }

    // All-args constructor
    public LeaveBalanceResponseDTO(UUID id, UUID userId, String userName, LeaveType type,
                                 Integer year, Integer totalDays, Integer usedDays,
                                 Integer remainingDays, BigDecimal monthlyAccrualRate,
                                 Integer carriedForwardDays, LocalDate lastAccrualDate,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.type = type;
        this.year = year;
        this.totalDays = totalDays;
        this.usedDays = usedDays;
        this.remainingDays = remainingDays;
        this.monthlyAccrualRate = monthlyAccrualRate;
        this.carriedForwardDays = carriedForwardDays;
        this.lastAccrualDate = lastAccrualDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Builder pattern
    public static LeaveBalanceResponseDTOBuilder builder() {
        return new LeaveBalanceResponseDTOBuilder();
    }

    public static class LeaveBalanceResponseDTOBuilder {
        private UUID id;
        private UUID userId;
        private String userName;
        private LeaveType type;
        private Integer year;
        private Integer totalDays;
        private Integer usedDays;
        private Integer remainingDays;
        private BigDecimal monthlyAccrualRate;
        private Integer carriedForwardDays;
        private LocalDate lastAccrualDate;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        LeaveBalanceResponseDTOBuilder() {
        }

        public LeaveBalanceResponseDTOBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder userId(UUID userId) {
            this.userId = userId;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder type(LeaveType type) {
            this.type = type;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder year(Integer year) {
            this.year = year;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder totalDays(Integer totalDays) {
            this.totalDays = totalDays;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder usedDays(Integer usedDays) {
            this.usedDays = usedDays;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder remainingDays(Integer remainingDays) {
            this.remainingDays = remainingDays;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder monthlyAccrualRate(BigDecimal monthlyAccrualRate) {
            this.monthlyAccrualRate = monthlyAccrualRate;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder carriedForwardDays(Integer carriedForwardDays) {
            this.carriedForwardDays = carriedForwardDays;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder lastAccrualDate(LocalDate lastAccrualDate) {
            this.lastAccrualDate = lastAccrualDate;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public LeaveBalanceResponseDTOBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public LeaveBalanceResponseDTO build() {
            return new LeaveBalanceResponseDTO(id, userId, userName, type, year,
                                             totalDays, usedDays, remainingDays,
                                             monthlyAccrualRate, carriedForwardDays,
                                             lastAccrualDate, createdAt, updatedAt);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveBalanceResponseDTO that = (LeaveBalanceResponseDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (type != that.type) return false;
        if (year != null ? !year.equals(that.year) : that.year != null) return false;
        if (totalDays != null ? !totalDays.equals(that.totalDays) : that.totalDays != null) return false;
        if (usedDays != null ? !usedDays.equals(that.usedDays) : that.usedDays != null) return false;
        if (remainingDays != null ? !remainingDays.equals(that.remainingDays) : that.remainingDays != null)
            return false;
        if (monthlyAccrualRate != null ? !monthlyAccrualRate.equals(that.monthlyAccrualRate) : that.monthlyAccrualRate != null)
            return false;
        if (carriedForwardDays != null ? !carriedForwardDays.equals(that.carriedForwardDays) : that.carriedForwardDays != null)
            return false;
        if (lastAccrualDate != null ? !lastAccrualDate.equals(that.lastAccrualDate) : that.lastAccrualDate != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        return updatedAt != null ? updatedAt.equals(that.updatedAt) : that.updatedAt == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (totalDays != null ? totalDays.hashCode() : 0);
        result = 31 * result + (usedDays != null ? usedDays.hashCode() : 0);
        result = 31 * result + (remainingDays != null ? remainingDays.hashCode() : 0);
        result = 31 * result + (monthlyAccrualRate != null ? monthlyAccrualRate.hashCode() : 0);
        result = 31 * result + (carriedForwardDays != null ? carriedForwardDays.hashCode() : 0);
        result = 31 * result + (lastAccrualDate != null ? lastAccrualDate.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "LeaveBalanceResponseDTO{" +
                "id=" + id +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", type=" + type +
                ", year=" + year +
                ", totalDays=" + totalDays +
                ", usedDays=" + usedDays +
                ", remainingDays=" + remainingDays +
                ", monthlyAccrualRate=" + monthlyAccrualRate +
                ", carriedForwardDays=" + carriedForwardDays +
                ", lastAccrualDate=" + lastAccrualDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 