package dev.elieweb.timeaway.leave.dto;

public class LeaveBalanceInfo {
    private Integer totalDays;
    private Integer usedDays;
    private Integer remainingDays;

    // Default constructor
    public LeaveBalanceInfo() {
    }

    // All-args constructor
    public LeaveBalanceInfo(Integer totalDays, Integer usedDays, Integer remainingDays) {
        this.totalDays = totalDays;
        this.usedDays = usedDays;
        this.remainingDays = remainingDays;
    }

    // Getters and setters
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

    // Builder pattern
    public static LeaveBalanceInfoBuilder builder() {
        return new LeaveBalanceInfoBuilder();
    }

    public static class LeaveBalanceInfoBuilder {
        private Integer totalDays;
        private Integer usedDays;
        private Integer remainingDays;

        LeaveBalanceInfoBuilder() {
        }

        public LeaveBalanceInfoBuilder totalDays(Integer totalDays) {
            this.totalDays = totalDays;
            return this;
        }

        public LeaveBalanceInfoBuilder usedDays(Integer usedDays) {
            this.usedDays = usedDays;
            return this;
        }

        public LeaveBalanceInfoBuilder remainingDays(Integer remainingDays) {
            this.remainingDays = remainingDays;
            return this;
        }

        public LeaveBalanceInfo build() {
            return new LeaveBalanceInfo(totalDays, usedDays, remainingDays);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveBalanceInfo that = (LeaveBalanceInfo) o;

        if (totalDays != null ? !totalDays.equals(that.totalDays) : that.totalDays != null) return false;
        if (usedDays != null ? !usedDays.equals(that.usedDays) : that.usedDays != null) return false;
        return remainingDays != null ? remainingDays.equals(that.remainingDays) : that.remainingDays == null;
    }

    @Override
    public int hashCode() {
        int result = totalDays != null ? totalDays.hashCode() : 0;
        result = 31 * result + (usedDays != null ? usedDays.hashCode() : 0);
        result = 31 * result + (remainingDays != null ? remainingDays.hashCode() : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "LeaveBalanceInfo{" +
                "totalDays=" + totalDays +
                ", usedDays=" + usedDays +
                ", remainingDays=" + remainingDays +
                '}';
    }
}