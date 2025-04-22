package dev.elieweb.timeaway.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Schema(description = "DTO for department operations in authentication context")
public class DepartmentDTO {
    @Schema(hidden = true)
    private UUID id;
    
    @Schema(description = "Name of the department", example = "Human Resources")
    @NotBlank(message = "Department name is required")
    private String name;
    
    @Schema(description = "Description of the department", example = "Handles all HR related activities")
    @NotBlank(message = "Department description is required")
    private String description;
    
    @Schema(hidden = true)
    private boolean deleted;

    // Default constructor
    public DepartmentDTO() {
        this.deleted = false;
    }

    // All-args constructor
    public DepartmentDTO(UUID id, String name, String description, boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deleted = deleted;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    // Builder pattern
    public static DepartmentDTOBuilder builder() {
        return new DepartmentDTOBuilder();
    }

    public static class DepartmentDTOBuilder {
        private UUID id;
        private String name;
        private String description;
        private boolean deleted = false; // Default value

        DepartmentDTOBuilder() {
        }

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

        public DepartmentDTOBuilder deleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public DepartmentDTO build() {
            return new DepartmentDTO(id, name, description, deleted);
        }
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepartmentDTO that = (DepartmentDTO) o;

        if (deleted != that.deleted) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!name.equals(that.name)) return false;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }

    // toString
    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deleted=" + deleted +
                '}';
    }
} 