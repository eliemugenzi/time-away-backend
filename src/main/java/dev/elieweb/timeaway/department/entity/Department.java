package dev.elieweb.timeaway.department.entity;

import dev.elieweb.timeaway.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "departments")
public class Department extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean active = true;

    public Department() {}

    public Department(String name, String description, boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public static DepartmentBuilder builder() {
        return new DepartmentBuilder();
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }

    public static class DepartmentBuilder {
        private String name;
        private String description;
        private boolean active = true;

        DepartmentBuilder() {}

        public DepartmentBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DepartmentBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DepartmentBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Department build() {
            return new Department(name, description, active);
        }
    }
} 