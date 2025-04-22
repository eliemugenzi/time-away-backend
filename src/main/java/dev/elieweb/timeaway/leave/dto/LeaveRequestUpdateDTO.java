package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO for updating a leave request status")
public class LeaveRequestUpdateDTO {
    @Schema(description = "Updated status of the leave request", example = "APPROVED")
    @NotNull(message = "Status is required")
    private LeaveStatus status;

    @Schema(description = "Reason for rejection (required only when status is REJECTED)", example = "Insufficient team coverage")
    private String rejectionReason;

    // Default constructor
    public LeaveRequestUpdateDTO() {
    }

    // All-args constructor
    public LeaveRequestUpdateDTO(LeaveStatus status, String rejectionReason) {
        this.status = status;
        this.rejectionReason = rejectionReason;
    }

    // Getters and setters
    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    // Builder pattern
    public static LeaveRequestUpdateDTOBuilder builder() {
        return new LeaveRequestUpdateDTOBuilder();
    }

    public static class LeaveRequestUpdateDTOBuilder {
        private LeaveStatus status;
        private String rejectionReason;

        LeaveRequestUpdateDTOBuilder() {
        }

        public LeaveRequestUpdateDTOBuilder status(LeaveStatus status) {
            this.status = status;
            return this;
        }

        public LeaveRequestUpdateDTOBuilder rejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public LeaveRequestUpdateDTO build() {
            return new LeaveRequestUpdateDTO(status, rejectionReason);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveRequestUpdateDTO that = (LeaveRequestUpdateDTO) o;

        if (status != that.status) return false;
        return rejectionReason != null ? rejectionReason.equals(that.rejectionReason) : that.rejectionReason == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (rejectionReason != null ? rejectionReason.hashCode() : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "LeaveRequestUpdateDTO{" +
                "status=" + status +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }
} 