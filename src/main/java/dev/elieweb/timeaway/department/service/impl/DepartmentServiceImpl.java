package dev.elieweb.timeaway.department.service.impl;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.service.CurrentUserService;
import dev.elieweb.timeaway.department.dto.DepartmentDTO;
import dev.elieweb.timeaway.department.dto.DepartmentResponseDTO;
import dev.elieweb.timeaway.department.entity.Department;
import dev.elieweb.timeaway.department.repository.DepartmentRepository;
import dev.elieweb.timeaway.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final CurrentUserService currentUserService;

    @Override
    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        return departments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentResponseDTO> getActiveDepartments() {
        List<Department> departments = departmentRepository.findByDeletedFalse();
        return departments.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentResponseDTO getDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return mapToResponseDTO(department);
    }

    @Override
    @Transactional
    public DepartmentResponseDTO createDepartment(DepartmentDTO request) {
        Department department = Department.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        department = departmentRepository.save(department);
        return mapToResponseDTO(department);
    }

    @Override
    @Transactional
    public DepartmentResponseDTO updateDepartment(UUID id, DepartmentDTO request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.setName(request.getName());
        department.setDescription(request.getDescription());

        department = departmentRepository.save(department);
        return mapToResponseDTO(department);
    }

    @Override
    @Transactional
    public void deleteDepartment(UUID id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Check if any users are assigned to this department
        List<User> usersInDepartment = currentUserService.findUsersByDepartment(department);
        if (!usersInDepartment.isEmpty()) {
            throw new RuntimeException("Cannot delete department as it has users assigned to it");
        }

        departmentRepository.delete(department);
    }

    private DepartmentResponseDTO mapToResponseDTO(Department department) {
        return DepartmentResponseDTO.builder()
                .id(department.getId())
                .name(department.getName())
                .description(department.getDescription())
                .createdAt(department.getCreatedAt())
                .updatedAt(department.getUpdatedAt())
                .build();
    }
} 