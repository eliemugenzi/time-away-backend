package dev.elieweb.timeaway.department.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
} 