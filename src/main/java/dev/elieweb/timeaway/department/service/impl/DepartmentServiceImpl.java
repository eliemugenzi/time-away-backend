package dev.elieweb.timeaway.department.service.impl;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.common.exception.ResourceNotFoundException;
import dev.elieweb.timeaway.department.dto.DepartmentDTO;
import dev.elieweb.timeaway.department.entity.Department;
import dev.elieweb.timeaway.department.repository.DepartmentRepository;
import dev.elieweb.timeaway.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Override
    public ApiResponse createDepartment(DepartmentDTO departmentDTO) {
        Department department = Department.builder()
                .name(departmentDTO.getName())
                .description(departmentDTO.getDescription())
                .build();

        departmentRepository.save(department);
        return ApiResponse.success("Department created successfully");
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentDTO> getActiveDepartments() {
        return departmentRepository.findByDeletedFalseOrderByCreatedAtDesc().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
        return mapToDTO(department);
    }

    @Override
    public ApiResponse updateDepartment(UUID id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        department.setName(departmentDTO.getName());
        department.setDescription(departmentDTO.getDescription());

        departmentRepository.save(department);
        return ApiResponse.success("Department updated successfully");
    }

    @Override
    public ApiResponse deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        departmentRepository.delete(department);
        return ApiResponse.success("Department deleted successfully");
    }

    private DepartmentDTO mapToDTO(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
} 