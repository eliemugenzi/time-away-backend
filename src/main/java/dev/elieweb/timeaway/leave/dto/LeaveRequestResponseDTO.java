package dev.elieweb.timeaway.leave.dto;

import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class LeaveRequestResponseDTO {
    @Schema(hidden = true)
    private UUID id;
    private String employeeName;
    private UUID departmentId;
    private LeaveType type;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private LeaveStatus status;
    private String rejectionReason;
    
    @Schema(description = "Details of the user who approved/rejected the request")
    private ApproverDTO approver;

    @Schema(description = "URL to access the supporting document")
    private String supportingDocumentUrl;

    @Schema(description = "Original name of the supporting document")
    private String supportingDocumentName;
    
    @Schema(hidden = true)
    private LocalDateTime createdAt;
    @Schema(hidden = true)
    private LocalDateTime updatedAt;

    // Default constructor
    public LeaveRequestResponseDTO() {
    }

    // All-args constructor
    public LeaveRequestResponseDTO(UUID id, String employeeName, UUID departmentId, LeaveType type,
                                 LocalDate startDate, LocalDate endDate, String reason,
                                 LeaveStatus status, String rejectionReason,
                                 ApproverDTO approver, String supportingDocumentUrl,
                                 String supportingDocumentName, LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
        this.id = id;
        this.employeeName = employeeName;
        this.departmentId = departmentId;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.approver = approver;
        this.supportingDocumentUrl = supportingDocumentUrl;
        this.supportingDocumentName = supportingDocumentName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
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

    public ApproverDTO getApprover() {
        return approver;
    }

    public void setApprover(ApproverDTO approver) {
        this.approver = approver;
    }

    public String getSupportingDocumentUrl() {
        return supportingDocumentUrl;
    }

    public void setSupportingDocumentUrl(String supportingDocumentUrl) {
        this.supportingDocumentUrl = supportingDocumentUrl;
    }

    public String getSupportingDocumentName() {
        return supportingDocumentName;
    }

    public void setSupportingDocumentName(String supportingDocumentName) {
        this.supportingDocumentName = supportingDocumentName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Builder pattern
    public static LeaveRequestResponseDTOBuilder builder() {
        return new LeaveRequestResponseDTOBuilder();
    }

    public static class LeaveRequestResponseDTOBuilder {
        private UUID id;
        private String employeeName;
        private UUID departmentId;
        private LeaveType type;
        private LocalDate startDate;
        private LocalDate endDate;
        private String reason;
        private LeaveStatus status;
        private String rejectionReason;
        private ApproverDTO approver;
        private String supportingDocumentUrl;
        private String supportingDocumentName;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        LeaveRequestResponseDTOBuilder() {
        }

        public LeaveRequestResponseDTOBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public LeaveRequestResponseDTOBuilder employeeName(String employeeName) {
            this.employeeName = employeeName;
            return this;
        }

        public LeaveRequestResponseDTOBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public LeaveRequestResponseDTOBuilder type(LeaveType type) {
            this.type = type;
            return this;
        }

        public LeaveRequestResponseDTOBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public LeaveRequestResponseDTOBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public LeaveRequestResponseDTOBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public LeaveRequestResponseDTOBuilder status(LeaveStatus status) {
            this.status = status;
            return this;
        }

        public LeaveRequestResponseDTOBuilder rejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public LeaveRequestResponseDTOBuilder approver(ApproverDTO approver) {
            this.approver = approver;
            return this;
        }

        public LeaveRequestResponseDTOBuilder supportingDocumentUrl(String supportingDocumentUrl) {
            this.supportingDocumentUrl = supportingDocumentUrl;
            return this;
        }

        public LeaveRequestResponseDTOBuilder supportingDocumentName(String supportingDocumentName) {
            this.supportingDocumentName = supportingDocumentName;
            return this;
        }

        public LeaveRequestResponseDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public LeaveRequestResponseDTOBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public LeaveRequestResponseDTO build() {
            return new LeaveRequestResponseDTO(id, employeeName, departmentId, type, startDate,
                                            endDate, reason, status, rejectionReason,
                                            approver, supportingDocumentUrl,
                                            supportingDocumentName, createdAt, updatedAt);
        }
    }

    // Inner ApproverDTO class
    public static class ApproverDTO {
        private UUID id;
        private String firstName;
        private String lastName;
        private String email;

        // Default constructor
        public ApproverDTO() {
        }

        // All-args constructor
        public ApproverDTO(UUID id, String firstName, String lastName, String email) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
        }

        // Getters and setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        // Builder pattern
        public static ApproverDTOBuilder builder() {
            return new ApproverDTOBuilder();
        }

        public static class ApproverDTOBuilder {
            private UUID id;
            private String firstName;
            private String lastName;
            private String email;

            ApproverDTOBuilder() {
            }

            public ApproverDTOBuilder id(UUID id) {
                this.id = id;
                return this;
            }

            public ApproverDTOBuilder firstName(String firstName) {
                this.firstName = firstName;
                return this;
            }

            public ApproverDTOBuilder lastName(String lastName) {
                this.lastName = lastName;
                return this;
            }

            public ApproverDTOBuilder email(String email) {
                this.email = email;
                return this;
            }

            public ApproverDTO build() {
                return new ApproverDTO(id, firstName, lastName, email);
            }
        }

        // equals and hashCode
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ApproverDTO that = (ApproverDTO) o;

            if (id != null ? !id.equals(that.id) : that.id != null) return false;
            if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
            if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
            return email != null ? email.equals(that.email) : that.email == null;
        }

        @Override
        public int hashCode() {
            int result = id != null ? id.hashCode() : 0;
            result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
            result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
            result = 31 * result + (email != null ? email.hashCode() : 0);
            return result;
        }

        // toString
        @Override
        public String toString() {
            return "ApproverDTO{" +
                    "id=" + id +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LeaveRequestResponseDTO that = (LeaveRequestResponseDTO) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (employeeName != null ? !employeeName.equals(that.employeeName) : that.employeeName != null)
            return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null)
            return false;
        if (type != that.type) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;
        if (status != that.status) return false;
        if (rejectionReason != null ? !rejectionReason.equals(that.rejectionReason) : that.rejectionReason != null)
            return false;
        if (approver != null ? !approver.equals(that.approver) : that.approver != null) return false;
        if (supportingDocumentUrl != null ? !supportingDocumentUrl.equals(that.supportingDocumentUrl) : that.supportingDocumentUrl != null)
            return false;
        if (supportingDocumentName != null ? !supportingDocumentName.equals(that.supportingDocumentName) : that.supportingDocumentName != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        return updatedAt != null ? updatedAt.equals(that.updatedAt) : that.updatedAt == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (employeeName != null ? employeeName.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (rejectionReason != null ? rejectionReason.hashCode() : 0);
        result = 31 * result + (approver != null ? approver.hashCode() : 0);
        result = 31 * result + (supportingDocumentUrl != null ? supportingDocumentUrl.hashCode() : 0);
        result = 31 * result + (supportingDocumentName != null ? supportingDocumentName.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "LeaveRequestResponseDTO{" +
                "id=" + id +
                ", employeeName='" + employeeName + '\'' +
                ", departmentId=" + departmentId +
                ", type=" + type +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", approver=" + approver +
                ", supportingDocumentUrl='" + supportingDocumentUrl + '\'' +
                ", supportingDocumentName='" + supportingDocumentName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 