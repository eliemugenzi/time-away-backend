package dev.elieweb.timeaway.department.service;

import dev.elieweb.timeaway.department.dto.DepartmentDTO;
import dev.elieweb.timeaway.department.dto.DepartmentResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    List<DepartmentResponseDTO> getAllDepartments();
    List<DepartmentResponseDTO> getActiveDepartments();
    DepartmentResponseDTO getDepartment(UUID id);
    DepartmentResponseDTO createDepartment(DepartmentDTO request);
    DepartmentResponseDTO updateDepartment(UUID id, DepartmentDTO request);
    void deleteDepartment(UUID id);
} 