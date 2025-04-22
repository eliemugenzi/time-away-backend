package dev.elieweb.timeaway.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.Objects;

@Schema(description = "DTO for department operations")
public class DepartmentDTO {
    @Schema(hidden = true)
    private UUID id;

    @Schema(description = "Name of the department", example = "Human Resources")
    @NotBlank(message = "Department name is required")
    private String name;

    @Schema(description = "Description of the department", example = "Handles all HR related activities")
    private String description;

    @Schema(hidden = true)
    private LocalDateTime createdAt;
    
    @Schema(hidden = true)
    private LocalDateTime updatedAt;

    public DepartmentDTO() {}

    public DepartmentDTO(UUID id, String name, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static DepartmentDTOBuilder builder() {
        return new DepartmentDTOBuilder();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DepartmentDTO that)) return false;
        return Objects.equals(id, that.id) &&
               Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static class DepartmentDTOBuilder {
        private UUID id;
        private String name;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        DepartmentDTOBuilder() {}

        public DepartmentDTOBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public DepartmentDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DepartmentDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DepartmentDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DepartmentDTOBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DepartmentDTO build() {
            return new DepartmentDTO(id, name, description, createdAt, updatedAt);
        }
    }
} 