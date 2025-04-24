package dev.elieweb.timeaway.leave.entity;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.common.entity.BaseEntity;
import dev.elieweb.timeaway.department.entity.Department;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import dev.elieweb.timeaway.leave.enums.LeaveDurationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LeaveRequest extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

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

    private String rejectionReason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "supporting_document_url")
    private String supportingDocumentUrl;

    @Column(name = "supporting_document_name")
    private String supportingDocumentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveDurationType durationType;

    public double getDuration() {
        long days = calculateWorkingDays();
        return durationType == LeaveDurationType.HALF_DAY ? days / 2.0 : days;
    }

    private long calculateWorkingDays() {
        long days = 0;
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek().getValue() < 6) { // Monday = 1, Friday = 5
                days++;
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return days;
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
} 