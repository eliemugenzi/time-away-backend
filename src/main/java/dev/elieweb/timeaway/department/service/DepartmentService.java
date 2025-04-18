package dev.elieweb.timeaway.department.service;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.department.dto.DepartmentDTO;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    ApiResponse createDepartment(DepartmentDTO departmentDTO);
    List<DepartmentDTO> getAllDepartments();
    List<DepartmentDTO> getActiveDepartments();
    DepartmentDTO getDepartment(UUID id);
    ApiResponse updateDepartment(UUID id, DepartmentDTO departmentDTO);
    ApiResponse deleteDepartment(UUID id);
} 