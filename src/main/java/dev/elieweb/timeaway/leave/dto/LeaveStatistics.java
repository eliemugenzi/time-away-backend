package dev.elieweb.timeaway.leave.dto;

public class LeaveStatistics {
    private int totalRequests;
    private int pendingRequests;
    private int approvedRequests;
    private int rejectedRequests;
    private int cancelledRequests;
    private double averageLeaveDuration;

    // Default constructor
    public LeaveStatistics() {
    }

    // All-args constructor
    public LeaveStatistics(int totalRequests, int pendingRequests, int approvedRequests,
                         int rejectedRequests, int cancelledRequests, double averageLeaveDuration) {
        this.totalRequests = totalRequests;
        this.pendingRequests = pendingRequests;
        this.approvedRequests = approvedRequests;
        this.rejectedRequests = rejectedRequests;
        this.cancelledRequests = cancelledRequests;
        this.averageLeaveDuration = averageLeaveDuration;
    }

    // Getters and setters
    public int getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public int getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(int pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public int getApprovedRequests() {
        return approvedRequests;
    }

    public void setApprovedRequests(int approvedRequests) {
        this.approvedRequests = approvedRequests;
    }

    public int getRejectedRequests() {
        return rejectedRequests;
    }

    public void setRejectedRequests(int rejectedRequests) {
        this.rejectedRequests = rejectedRequests;
    }

    public int getCancelledRequests() {
        return cancelledRequests;
    }

    public void setCancelledRequests(int cancelledRequests) {
        this.cancelledRequests = cancelledRequests;
    }

    public double getAverageLeaveDuration() {
        return averageLeaveDuration;
    }

    public void setAverageLeaveDuration(double averageLeaveDuration) {
        this.averageLeaveDuration = averageLeaveDuration;
    }

    // Builder pattern
    public static LeaveStatisticsBuilder builder() {
        return new LeaveStatisticsBuilder();
    }

    public static class LeaveStatisticsBuilder {
        private int totalRequests;
        private int pendingRequests;
        private int approvedRequests;
        private int rejectedRequests;
        private int cancelledRequests;
        private double averageLeaveDuration;

        LeaveStatisticsBuilder() {
        }

        public LeaveStatisticsBuilder totalRequests(int totalRequests) {
            this.totalRequests = totalRequests;
            return this;
        }

        public LeaveStatisticsBuilder pendingRequests(int pendingRequests) {
            this.pendingRequests = pendingRequests;
            return this;
        }

        public LeaveStatisticsBuilder approvedRequests(int approvedRequests) {
            this.approvedRequests = approvedRequests;
            return this;
        }

        public LeaveStatisticsBuilder rejectedRequests(int rejectedRequests) {
            this.rejectedRequests = rejectedRequests;
            return this;
        }

        public LeaveStatisticsBuilder cancelledRequests(int cancelledRequests) {
            this.cancelledRequests = cancelledRequests;
            return this;
        }

        public LeaveStatisticsBuilder averageLeaveDuration(double averageLeaveDuration) {
            this.averageLeaveDuration = averageLeaveDuration;
            return this;
        }

        public LeaveStatistics build() {
            return new LeaveStatistics(totalRequests, pendingRequests, approvedRequests,
                                     rejectedRequests, cancelledRequests, averageLeaveDuration);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveStatistics that = (LeaveStatistics) o;

        if (totalRequests != that.totalRequests) return false;
        if (pendingRequests != that.pendingRequests) return false;
        if (approvedRequests != that.approvedRequests) return false;
        if (rejectedRequests != that.rejectedRequests) return false;
        if (cancelledRequests != that.cancelledRequests) return false;
        return Double.compare(that.averageLeaveDuration, averageLeaveDuration) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = totalRequests;
        result = 31 * result + pendingRequests;
        result = 31 * result + approvedRequests;
        result = 31 * result + rejectedRequests;
        result = 31 * result + cancelledRequests;
        temp = Double.doubleToLongBits(averageLeaveDuration);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "LeaveStatistics{" +
                "totalRequests=" + totalRequests +
                ", pendingRequests=" + pendingRequests +
                ", approvedRequests=" + approvedRequests +
                ", rejectedRequests=" + rejectedRequests +
                ", cancelledRequests=" + cancelledRequests +
                ", averageLeaveDuration=" + averageLeaveDuration +
                '}';
    }
} 