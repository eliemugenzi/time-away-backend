package dev.elieweb.timeaway.leave.dto;

public class DepartmentStatistics {
    private String department;
    private int totalEmployees;
    private int employeesOnLeave;
    private int totalLeaveRequests;
    private int approvedLeaveRequests;
    private int pendingLeaveRequests;
    private double averageLeaveDuration;

    // Default constructor
    public DepartmentStatistics() {
    }

    // All-args constructor
    public DepartmentStatistics(String department, int totalEmployees, int employeesOnLeave,
                              int totalLeaveRequests, int approvedLeaveRequests,
                              int pendingLeaveRequests, double averageLeaveDuration) {
        this.department = department;
        this.totalEmployees = totalEmployees;
        this.employeesOnLeave = employeesOnLeave;
        this.totalLeaveRequests = totalLeaveRequests;
        this.approvedLeaveRequests = approvedLeaveRequests;
        this.pendingLeaveRequests = pendingLeaveRequests;
        this.averageLeaveDuration = averageLeaveDuration;
    }

    // Getters and setters
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public int getEmployeesOnLeave() {
        return employeesOnLeave;
    }

    public void setEmployeesOnLeave(int employeesOnLeave) {
        this.employeesOnLeave = employeesOnLeave;
    }

    public int getTotalLeaveRequests() {
        return totalLeaveRequests;
    }

    public void setTotalLeaveRequests(int totalLeaveRequests) {
        this.totalLeaveRequests = totalLeaveRequests;
    }

    public int getApprovedLeaveRequests() {
        return approvedLeaveRequests;
    }

    public void setApprovedLeaveRequests(int approvedLeaveRequests) {
        this.approvedLeaveRequests = approvedLeaveRequests;
    }

    public int getPendingLeaveRequests() {
        return pendingLeaveRequests;
    }

    public void setPendingLeaveRequests(int pendingLeaveRequests) {
        this.pendingLeaveRequests = pendingLeaveRequests;
    }

    public double getAverageLeaveDuration() {
        return averageLeaveDuration;
    }

    public void setAverageLeaveDuration(double averageLeaveDuration) {
        this.averageLeaveDuration = averageLeaveDuration;
    }

    // Builder pattern
    public static DepartmentStatisticsBuilder builder() {
        return new DepartmentStatisticsBuilder();
    }

    public static class DepartmentStatisticsBuilder {
        private String department;
        private int totalEmployees;
        private int employeesOnLeave;
        private int totalLeaveRequests;
        private int approvedLeaveRequests;
        private int pendingLeaveRequests;
        private double averageLeaveDuration;

        DepartmentStatisticsBuilder() {
        }

        public DepartmentStatisticsBuilder department(String department) {
            this.department = department;
            return this;
        }

        public DepartmentStatisticsBuilder totalEmployees(int totalEmployees) {
            this.totalEmployees = totalEmployees;
            return this;
        }

        public DepartmentStatisticsBuilder employeesOnLeave(int employeesOnLeave) {
            this.employeesOnLeave = employeesOnLeave;
            return this;
        }

        public DepartmentStatisticsBuilder totalLeaveRequests(int totalLeaveRequests) {
            this.totalLeaveRequests = totalLeaveRequests;
            return this;
        }

        public DepartmentStatisticsBuilder approvedLeaveRequests(int approvedLeaveRequests) {
            this.approvedLeaveRequests = approvedLeaveRequests;
            return this;
        }

        public DepartmentStatisticsBuilder pendingLeaveRequests(int pendingLeaveRequests) {
            this.pendingLeaveRequests = pendingLeaveRequests;
            return this;
        }

        public DepartmentStatisticsBuilder averageLeaveDuration(double averageLeaveDuration) {
            this.averageLeaveDuration = averageLeaveDuration;
            return this;
        }

        public DepartmentStatistics build() {
            return new DepartmentStatistics(department, totalEmployees, employeesOnLeave,
                                          totalLeaveRequests, approvedLeaveRequests,
                                          pendingLeaveRequests, averageLeaveDuration);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepartmentStatistics that = (DepartmentStatistics) o;

        if (totalEmployees != that.totalEmployees) return false;
        if (employeesOnLeave != that.employeesOnLeave) return false;
        if (totalLeaveRequests != that.totalLeaveRequests) return false;
        if (approvedLeaveRequests != that.approvedLeaveRequests) return false;
        if (pendingLeaveRequests != that.pendingLeaveRequests) return false;
        if (Double.compare(that.averageLeaveDuration, averageLeaveDuration) != 0) return false;
        return department.equals(that.department);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = department.hashCode();
        result = 31 * result + totalEmployees;
        result = 31 * result + employeesOnLeave;
        result = 31 * result + totalLeaveRequests;
        result = 31 * result + approvedLeaveRequests;
        result = 31 * result + pendingLeaveRequests;
        temp = Double.doubleToLongBits(averageLeaveDuration);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "DepartmentStatistics{" +
                "department='" + department + '\'' +
                ", totalEmployees=" + totalEmployees +
                ", employeesOnLeave=" + employeesOnLeave +
                ", totalLeaveRequests=" + totalLeaveRequests +
                ", approvedLeaveRequests=" + approvedLeaveRequests +
                ", pendingLeaveRequests=" + pendingLeaveRequests +
                ", averageLeaveDuration=" + averageLeaveDuration +
                '}';
    }
} 