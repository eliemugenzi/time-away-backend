package dev.elieweb.timeaway.department.controller;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.department.dto.DepartmentDTO;
import dev.elieweb.timeaway.department.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "Department management endpoints")
public class DepartmentController {
    private final DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Create a new department")
    public ResponseEntity<ApiResponse> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(departmentService.createDepartment(departmentDTO));
    }

    @GetMapping
    @Operation(summary = "Get all departments")
    public ResponseEntity<ApiResponse> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved successfully", departments));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active departments")
    public ResponseEntity<ApiResponse> getActiveDepartments() {
        List<DepartmentDTO> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(ApiResponse.success("Active departments retrieved successfully", departments));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<ApiResponse> getDepartment(@PathVariable UUID id) {
        DepartmentDTO department = departmentService.getDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved successfully", department));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department")
    public ResponseEntity<ApiResponse> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentDTO departmentDTO
    ) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department")
    public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable UUID id) {
        return ResponseEntity.ok(departmentService.deleteDepartment(id));
    }
}