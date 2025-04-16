package dev.elieweb.timeaway.leave.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentStatistics {
    private String department;
    private int totalEmployees;
    private int employeesOnLeave;
    private int totalLeaveRequests;
    private int approvedLeaveRequests;
    private int pendingLeaveRequests;
    private double averageLeaveDuration;
} 