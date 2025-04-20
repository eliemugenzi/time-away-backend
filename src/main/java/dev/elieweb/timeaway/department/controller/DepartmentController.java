package dev.elieweb.timeaway.department.controller;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.department.dto.DepartmentDTO;
import dev.elieweb.timeaway.department.dto.DepartmentResponseDTO;
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
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", departmentService.createDepartment(departmentDTO)));
    }

    @GetMapping
    @Operation(summary = "Get all departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved successfully", departments));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active departments")
    public ResponseEntity<ApiResponse<List<DepartmentResponseDTO>>> getActiveDepartments() {
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success("Active departments retrieved successfully", departments));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> getDepartment(@PathVariable UUID id) {
        DepartmentResponseDTO department = departmentService.getDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved successfully", department));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department")
    public ResponseEntity<ApiResponse<DepartmentResponseDTO>> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentDTO departmentDTO
    ) {
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", 
            departmentService.updateDepartment(id, departmentDTO)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully", null));
    }
}