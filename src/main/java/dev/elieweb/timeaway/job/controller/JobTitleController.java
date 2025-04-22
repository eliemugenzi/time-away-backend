package dev.elieweb.timeaway.job.controller;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.job.dto.JobTitleDTO;
import dev.elieweb.timeaway.job.service.JobTitleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/job-titles")
@RequiredArgsConstructor
@Tag(name = "Job Titles", description = "Job title management endpoints")
public class JobTitleController {
    private final JobTitleService jobTitleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new job title")
    public ResponseEntity<ApiResponse<JobTitleDTO>> createJobTitle(@Valid @RequestBody JobTitleDTO jobTitleDTO) {
        ApiResponse<JobTitleDTO> response = jobTitleService.createJobTitle(jobTitleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get all job titles")
    public ResponseEntity<ApiResponse<List<JobTitleDTO>>> getAllJobTitles() {
        List<JobTitleDTO> jobTitles = jobTitleService.getAllJobTitles();
        return ResponseEntity.ok(ApiResponse.success("Job titles retrieved successfully", jobTitles));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active job titles")
    public ResponseEntity<ApiResponse<List<JobTitleDTO>>> getActiveJobTitles() {
        List<JobTitleDTO> jobTitles = jobTitleService.getActiveJobTitles();
        return ResponseEntity.ok(ApiResponse.success("Active job titles retrieved successfully", jobTitles));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job title by ID")
    public ResponseEntity<ApiResponse<JobTitleDTO>> getJobTitle(@PathVariable UUID id) {
        JobTitleDTO jobTitle = jobTitleService.getJobTitle(id);
        return ResponseEntity.ok(ApiResponse.success("Job title retrieved successfully", jobTitle));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update job title")
    public ResponseEntity<ApiResponse<JobTitleDTO>> updateJobTitle(
            @PathVariable UUID id,
            @Valid @RequestBody JobTitleDTO jobTitleDTO
    ) {
        return ResponseEntity.ok(ApiResponse.success("Job title updated successfully", 
            jobTitleService.updateJobTitle(id, jobTitleDTO).getData()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete job title")
    public ResponseEntity<ApiResponse<Void>> deleteJobTitle(@PathVariable UUID id) {
        return ResponseEntity.ok(jobTitleService.deleteJobTitle(id));
    }

    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Get job titles by department")
    public ResponseEntity<ApiResponse<List<JobTitleDTO>>> getJobTitlesByDepartment(@PathVariable UUID departmentId) {
        return ResponseEntity.ok(ApiResponse.success("Job titles retrieved successfully", 
            jobTitleService.getJobTitlesByDepartment(departmentId)));
    }
} 