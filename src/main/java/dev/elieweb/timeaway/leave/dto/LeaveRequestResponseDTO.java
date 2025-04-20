package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestResponseDTO {
    @Schema(hidden = true)
    private UUID id;
    private String employeeName;
    private LeaveType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
    private String rejectionReason;
    
    @Schema(description = "Details of the user who approved/rejected the request")
    private ApproverDTO approver;
    
    @Schema(hidden = true)
    private LocalDateTime createdAt;
    @Schema(hidden = true)
    private LocalDateTime updatedAt;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApproverDTO {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;
    }
} 