package dev.elieweb.timeaway.auth.service;

import dev.elieweb.timeaway.auth.dto.DepartmentDTO;
import dev.elieweb.timeaway.common.exception.ResourceNotFoundException;
import dev.elieweb.timeaway.department.entity.Department;
import dev.elieweb.timeaway.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<DepartmentDTO> getActiveDepartments() {
        return departmentRepository.findByDeletedFalse().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Department name already exists");
        }

        var department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        departmentRepository.save(department);
        return mapToDTO(department);
    }

    @Transactional
    public DepartmentDTO updateDepartment(UUID id, DepartmentDTO request) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        if (!department.getName().equals(request.getName()) &&
                departmentRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Department name already exists");
        }

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        departmentRepository.save(department);
        return mapToDTO(department);
    }

    @Transactional
    public void deleteDepartment(UUID id) {
        var department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        department.setDeleted(true);
        departmentRepository.save(department);
    }

    private DepartmentDTO mapToDTO(Department department) {
        return DepartmentDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .deleted(department.isDeleted())
                .build();
    }
} 