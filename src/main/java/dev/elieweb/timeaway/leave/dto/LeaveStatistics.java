package dev.elieweb.timeaway.leave.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveStatistics {
    private int totalRequests;
    private int pendingRequests;
    private int approvedRequests;
    private int rejectedRequests;
    private int cancelledRequests;
    private double averageLeaveDuration;
} 