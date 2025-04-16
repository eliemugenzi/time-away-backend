package dev.elieweb.timeaway.leave.dto.statistics;

import dev.elieweb.timeaway.leave.enums.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveStatistics {
    private int totalLeaveRequests;
    private int approvedRequests;
    private int pendingRequests;
    private int rejectedRequests;
    private Map<LeaveType, Integer> leaveTypeDistribution;
} 