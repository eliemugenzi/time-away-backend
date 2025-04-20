package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceResponseDTO {
    private UUID id;
    private UUID userId;
    private String userName;
    private LeaveType type;
    private Integer year;
    private Integer totalDays;
    private Integer usedDays;
    private Integer remainingDays;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 