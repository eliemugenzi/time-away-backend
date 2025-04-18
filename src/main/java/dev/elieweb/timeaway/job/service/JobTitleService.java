package dev.elieweb.timeaway.job.service;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.common.exception.ResourceNotFoundException;
import dev.elieweb.timeaway.department.entity.Department;
import dev.elieweb.timeaway.department.repository.DepartmentRepository;
import dev.elieweb.timeaway.job.dto.JobTitleDTO;
import dev.elieweb.timeaway.job.entity.JobTitle;
import dev.elieweb.timeaway.job.repository.JobTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobTitleService {
    private final JobTitleRepository jobTitleRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public ApiResponse createJobTitle(JobTitleDTO jobTitleDTO) {
        if (jobTitleRepository.existsByNameIgnoreCase(jobTitleDTO.getName())) {
            throw new IllegalArgumentException("Job title with name '" + jobTitleDTO.getName() + "' already exists");
        }

        Department department = departmentRepository.findByIdAndDeletedFalse(jobTitleDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + jobTitleDTO.getDepartmentId()));

        JobTitle jobTitle = JobTitle.builder()
                .name(jobTitleDTO.getName().trim())
                .description(jobTitleDTO.getDescription().trim())
                .department(department)
                .deleted(false)
                .build();

        try {
            JobTitle savedJobTitle = jobTitleRepository.save(jobTitle);
            return ApiResponse.success("Job title created successfully", mapToDTO(savedJobTitle));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Failed to create job title due to data integrity violation", e);
        }
    }

    public List<JobTitleDTO> getAllJobTitles() {
        return jobTitleRepository.findByDeletedFalseOrderByNameAsc()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public List<JobTitleDTO> getActiveJobTitles() {
        return jobTitleRepository.findByDeletedFalseOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    public JobTitleDTO getJobTitle(UUID id) {
        return jobTitleRepository.findByIdAndDeletedFalse(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Job title not found with id: " + id));
    }

    @Transactional
    public ApiResponse updateJobTitle(UUID id, JobTitleDTO jobTitleDTO) {
        JobTitle existingJobTitle = jobTitleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job title not found with id: " + id));

        if (!existingJobTitle.getName().equalsIgnoreCase(jobTitleDTO.getName()) &&
                jobTitleRepository.existsByNameIgnoreCase(jobTitleDTO.getName())) {
            throw new IllegalArgumentException("Job title with name '" + jobTitleDTO.getName() + "' already exists");
        }

        Department department = departmentRepository.findByIdAndDeletedFalse(jobTitleDTO.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + jobTitleDTO.getDepartmentId()));

        existingJobTitle.setName(jobTitleDTO.getName().trim());
        existingJobTitle.setDescription(jobTitleDTO.getDescription().trim());
        existingJobTitle.setDepartment(department);

        try {
            JobTitle updatedJobTitle = jobTitleRepository.save(existingJobTitle);
            return ApiResponse.success("Job title updated successfully", mapToDTO(updatedJobTitle));
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Failed to update job title due to data integrity violation", e);
        }
    }

    @Transactional
    public ApiResponse deleteJobTitle(UUID id) {
        JobTitle jobTitle = jobTitleRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job title not found with id: " + id));

        jobTitle.setDeleted(true);
        jobTitleRepository.save(jobTitle);
        return ApiResponse.success("Job title deleted successfully");
    }

    private JobTitleDTO mapToDTO(JobTitle jobTitle) {
        return JobTitleDTO.builder()
                .id(jobTitle.getId())
                .name(jobTitle.getName())
                .description(jobTitle.getDescription())
                .departmentId(jobTitle.getDepartment().getId())
                .departmentName(jobTitle.getDepartment().getName())
                .deleted(jobTitle.isDeleted())
                .createdAt(jobTitle.getCreatedAt())
                .updatedAt(jobTitle.getUpdatedAt())
                .build();
    }
} 