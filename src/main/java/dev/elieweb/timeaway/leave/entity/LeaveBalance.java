package dev.elieweb.timeaway.leave.entity;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.common.entity.BaseEntity;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "leave_balances")
public class LeaveBalance extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType type;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer totalDays;

    @Column(nullable = false)
    private Integer usedDays;

    @Column(nullable = false)
    private Integer remainingDays;
} 