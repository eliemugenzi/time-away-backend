package dev.elieweb.timeaway.leave.entity;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.common.entity.BaseEntity;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType type;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;

    @Column
    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    public LeaveRequest() {}

    public LeaveRequest(User user, LeaveType type, LocalDate startDate, LocalDate endDate,
                       String reason, LeaveStatus status, String rejectionReason, User approver) {
        this.user = user;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
        this.rejectionReason = rejectionReason;
        this.approver = approver;
    }

    public static LeaveRequestBuilder builder() {
        return new LeaveRequestBuilder();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public LeaveType getLeaveType() {
        return type;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + getId() +
                ", user=" + (user != null ? user.getEmail() : null) +
                ", type=" + type +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", approver=" + (approver != null ? approver.getEmail() : null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeaveRequest that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(startDate, that.startDate) &&
               Objects.equals(endDate, that.endDate) &&
               Objects.equals(user, that.user) &&
               type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), user, type, startDate, endDate);
    }

    public static class LeaveRequestBuilder {
        private User user;
        private LeaveType type;
        private LocalDate startDate;
        private LocalDate endDate;
        private String reason;
        private LeaveStatus status;
        private String rejectionReason;
        private User approver;

        LeaveRequestBuilder() {}

        public LeaveRequestBuilder user(User user) {
            this.user = user;
            return this;
        }

        public LeaveRequestBuilder type(LeaveType type) {
            this.type = type;
            return this;
        }

        public LeaveRequestBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public LeaveRequestBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public LeaveRequestBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public LeaveRequestBuilder status(LeaveStatus status) {
            this.status = status;
            return this;
        }

        public LeaveRequestBuilder rejectionReason(String rejectionReason) {
            this.rejectionReason = rejectionReason;
            return this;
        }

        public LeaveRequestBuilder approver(User approver) {
            this.approver = approver;
            return this;
        }

        public LeaveRequest build() {
            return new LeaveRequest(user, type, startDate, endDate, reason, status, rejectionReason, approver);
        }
    }
} 