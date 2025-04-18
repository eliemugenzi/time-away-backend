package dev.elieweb.timeaway.job.entity;

import dev.elieweb.timeaway.common.entity.BaseEntity;
import dev.elieweb.timeaway.department.entity.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "job_titles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobTitle extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean deleted = false;
} 