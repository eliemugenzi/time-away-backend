package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a leave request")
public class LeaveRequestDTO {
    @Schema(description = "Type of leave", example = "ANNUAL")
    @NotNull(message = "Leave type is required")
    private LeaveType type;

    @Schema(description = "Start date of leave", example = "2024-03-20")
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Schema(description = "End date of leave", example = "2024-03-25")
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Schema(description = "Reason for leave request", example = "Family vacation")
    @NotBlank(message = "Reason is required")
    private String reason;
} 