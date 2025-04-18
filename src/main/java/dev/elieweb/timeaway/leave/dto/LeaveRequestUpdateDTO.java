package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for updating a leave request status")
public class LeaveRequestUpdateDTO {
    @Schema(description = "Updated status of the leave request", example = "APPROVED")
    @NotNull(message = "Status is required")
    private LeaveStatus status;

    @Schema(description = "Reason for rejection (required only when status is REJECTED)", example = "Insufficient team coverage")
    private String rejectionReason;
} 