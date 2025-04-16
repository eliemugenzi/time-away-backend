package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveRequestUpdateDTO {
    @NotNull(message = "Status is required")
    private LeaveStatus status;

    private String rejectionReason;
} 