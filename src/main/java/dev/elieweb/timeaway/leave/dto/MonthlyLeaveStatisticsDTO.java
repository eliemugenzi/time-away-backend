package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveType;
import java.util.Map;
import java.util.HashMap;

public class MonthlyLeaveStatisticsDTO {
    private int year;
    private int month;
    private long approvedLeaveCount;
    private double totalLeaveDays;
    private String monthName;
    private Map<LeaveType, LeaveTypeStats> leaveTypeStatistics;

    public MonthlyLeaveStatisticsDTO() {
        this.leaveTypeStatistics = new HashMap<>();
    }

    public MonthlyLeaveStatisticsDTO(int year, int month, long approvedLeaveCount, double totalLeaveDays, String monthName, Map<LeaveType, LeaveTypeStats> leaveTypeStatistics) {
        this.year = year;
        this.month = month;
        this.approvedLeaveCount = approvedLeaveCount;
        this.totalLeaveDays = totalLeaveDays;
        this.monthName = monthName;
        this.leaveTypeStatistics = leaveTypeStatistics != null ? leaveTypeStatistics : new HashMap<>();
    }

    public static MonthlyLeaveStatisticsDTOBuilder builder() {
        return new MonthlyLeaveStatisticsDTOBuilder();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public long getApprovedLeaveCount() {
        return approvedLeaveCount;
    }

    public void setApprovedLeaveCount(long approvedLeaveCount) {
        this.approvedLeaveCount = approvedLeaveCount;
    }

    public double getTotalLeaveDays() {
        return totalLeaveDays;
    }

    public void setTotalLeaveDays(double totalLeaveDays) {
        this.totalLeaveDays = totalLeaveDays;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public Map<LeaveType, LeaveTypeStats> getLeaveTypeStatistics() {
        return leaveTypeStatistics;
    }

    public void setLeaveTypeStatistics(Map<LeaveType, LeaveTypeStats> leaveTypeStatistics) {
        this.leaveTypeStatistics = leaveTypeStatistics != null ? leaveTypeStatistics : new HashMap<>();
    }

    public static class LeaveTypeStats {
        private long count;
        private double totalDays;

        public LeaveTypeStats() {
            this.count = 0;
            this.totalDays = 0.0;
        }

        public LeaveTypeStats(long count, double totalDays) {
            this.count = count;
            this.totalDays = totalDays;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public double getTotalDays() {
            return totalDays;
        }

        public void setTotalDays(double totalDays) {
            this.totalDays = totalDays;
        }

        public void incrementCount() {
            this.count++;
        }

        public void addDays(double days) {
            this.totalDays += days;
        }
    }

    public static class MonthlyLeaveStatisticsDTOBuilder {
        private int year;
        private int month;
        private long approvedLeaveCount;
        private double totalLeaveDays;
        private String monthName;
        private Map<LeaveType, LeaveTypeStats> leaveTypeStatistics = new HashMap<>();

        MonthlyLeaveStatisticsDTOBuilder() {}

        public MonthlyLeaveStatisticsDTOBuilder year(int year) {
            this.year = year;
            return this;
        }

        public MonthlyLeaveStatisticsDTOBuilder month(int month) {
            this.month = month;
            return this;
        }

        public MonthlyLeaveStatisticsDTOBuilder approvedLeaveCount(long approvedLeaveCount) {
            this.approvedLeaveCount = approvedLeaveCount;
            return this;
        }

        public MonthlyLeaveStatisticsDTOBuilder totalLeaveDays(double totalLeaveDays) {
            this.totalLeaveDays = totalLeaveDays;
            return this;
        }

        public MonthlyLeaveStatisticsDTOBuilder monthName(String monthName) {
            this.monthName = monthName;
            return this;
        }

        public MonthlyLeaveStatisticsDTOBuilder leaveTypeStatistics(Map<LeaveType, LeaveTypeStats> leaveTypeStatistics) {
            this.leaveTypeStatistics = leaveTypeStatistics;
            return this;
        }

        public MonthlyLeaveStatisticsDTO build() {
            return new MonthlyLeaveStatisticsDTO(year, month, approvedLeaveCount, totalLeaveDays, monthName, leaveTypeStatistics);
        }
    }
}