package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

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

    public LeaveRequestDTO() {}

    public LeaveRequestDTO(LeaveType type, LocalDate startDate, LocalDate endDate, String reason) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }

    public static LeaveRequestDTOBuilder builder() {
        return new LeaveRequestDTOBuilder();
    }

    public LeaveType getType() {
        return type;
    }

    public void setType(LeaveType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "LeaveRequestDTO{" +
                "type=" + type +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaveRequestDTO that)) return false;
        return type == that.type &&
               Objects.equals(startDate, that.startDate) &&
               Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, startDate, endDate);
    }

    public static class LeaveRequestDTOBuilder {
        private LeaveType type;
        private LocalDate startDate;
        private LocalDate endDate;
        private String reason;

        LeaveRequestDTOBuilder() {}

        public LeaveRequestDTOBuilder type(LeaveType type) {
            this.type = type;
            return this;
        }

        public LeaveRequestDTOBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public LeaveRequestDTOBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public LeaveRequestDTOBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public LeaveRequestDTO build() {
            return new LeaveRequestDTO(type, startDate, endDate, reason);
        }
    }
} 