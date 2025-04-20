package dev.elieweb.timeaway.department.entity;

import dev.elieweb.timeaway.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "departments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_deleted")
    @Builder.Default
    private boolean deleted = false;
} 