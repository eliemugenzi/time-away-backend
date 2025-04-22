package dev.elieweb.timeaway.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "DTO for job title operations")
public class JobTitleDTO {
    @Schema(hidden = true)
    private UUID id;

    @Schema(description = "Name of the job title", example = "Software Engineer")
    @NotBlank(message = "Job title name is required")
    @Size(min = 2, max = 100, message = "Job title name must be between 2 and 100 characters")
    @Pattern(regexp = "^[\\p{L}\\s\\-&.()]+$", message = "Job title name can only contain letters, spaces, hyphens, ampersands, periods, and parentheses")
    private String name;

    @Schema(description = "Description of the job title", example = "Develops and maintains software applications")
    @NotBlank(message = "Job title description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @Schema(description = "ID of the department this job title belongs to")
    @NotNull(message = "Department ID is required")
    private UUID departmentId;

    @Schema(hidden = true)
    private String departmentName;

    @Schema(hidden = true)
    private boolean deleted;

    @Schema(hidden = true)
    private LocalDateTime createdAt;

    @Schema(hidden = true)
    private LocalDateTime updatedAt;

    // Default constructor
    public JobTitleDTO() {
        this.deleted = false;
    }

    // All-args constructor
    public JobTitleDTO(UUID id, String name, String description, UUID departmentId,
                      String departmentName, boolean deleted, LocalDateTime createdAt,
                      LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.deleted = deleted;
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

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
    public static JobTitleDTOBuilder builder() {
        return new JobTitleDTOBuilder();
    }

    public static class JobTitleDTOBuilder {
        private UUID id;
        private String name;
        private String description;
        private UUID departmentId;
        private String departmentName;
        private boolean deleted = false;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        JobTitleDTOBuilder() {
        }

        public JobTitleDTOBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public JobTitleDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public JobTitleDTOBuilder description(String description) {
            this.description = description;
            return this;
        }

        public JobTitleDTOBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public JobTitleDTOBuilder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public JobTitleDTOBuilder deleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public JobTitleDTOBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public JobTitleDTOBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public JobTitleDTO build() {
            return new JobTitleDTO(id, name, description, departmentId,
                                 departmentName, deleted, createdAt, updatedAt);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobTitleDTO that = (JobTitleDTO) o;

        if (deleted != that.deleted) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        if (!departmentId.equals(that.departmentId)) return false;
        if (departmentName != null ? !departmentName.equals(that.departmentName) : that.departmentName != null)
            return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        return updatedAt != null ? updatedAt.equals(that.updatedAt) : that.updatedAt == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + departmentId.hashCode();
        result = 31 * result + (departmentName != null ? departmentName.hashCode() : 0);
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "JobTitleDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                ", deleted=" + deleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
} 