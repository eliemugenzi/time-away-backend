package dev.elieweb.timeaway.leave.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceInfo {
    private Integer totalDays;
    private Integer usedDays;
    private Integer remainingDays;
} 