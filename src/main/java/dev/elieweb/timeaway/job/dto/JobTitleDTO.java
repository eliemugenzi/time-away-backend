package dev.elieweb.timeaway.job.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
} 