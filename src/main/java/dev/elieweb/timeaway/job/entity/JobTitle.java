package dev.elieweb.timeaway.job.entity;

import dev.elieweb.timeaway.common.entity.BaseEntity;
import dev.elieweb.timeaway.department.entity.Department;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "job_titles")
public class JobTitle extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    public JobTitle() {}

    public JobTitle(String name, String description, Department department) {
        this.name = name;
        this.description = description;
        this.department = department;
    }

    public static JobTitleBuilder builder() {
        return new JobTitleBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "JobTitle{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", department=" + (department != null ? department.getName() : null) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JobTitle jobTitle)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, jobTitle.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    public static class JobTitleBuilder {
        private String name;
        private String description;
        private Department department;

        JobTitleBuilder() {}

        public JobTitleBuilder name(String name) {
            this.name = name;
            return this;
        }

        public JobTitleBuilder description(String description) {
            this.description = description;
            return this;
        }

        public JobTitleBuilder department(Department department) {
            this.department = department;
            return this;
        }

        public JobTitle build() {
            return new JobTitle(name, description, department);
        }
    }
} 