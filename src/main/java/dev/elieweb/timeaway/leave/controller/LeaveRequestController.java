package dev.elieweb.timeaway.leave.controller;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestResponseDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestUpdateDTO;
import dev.elieweb.timeaway.leave.dto.PaginatedLeaveRequestResponse;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave Request", description = "Leave Request Management APIs")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @PostMapping
    @Operation(summary = "Create a new leave request")
    public ResponseEntity<ApiResponse<LeaveRequestResponseDTO>> createLeaveRequest(@Valid @RequestBody LeaveRequestDTO request) {
        LeaveRequestResponseDTO response = leaveRequestService.createLeaveRequest(request);
        return ResponseEntity.ok(ApiResponse.success("Leave request created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all leave requests with optional status filter and pagination")
    public ResponseEntity<ApiResponse<PaginatedLeaveRequestResponse>> getAllLeaveRequests(
            @RequestParam(required = false) LeaveStatus status,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNo,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        PaginatedLeaveRequestResponse response = leaveRequestService.getAllLeaveRequests(status, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success("Leave requests retrieved successfully", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get leave request by ID")
    public ResponseEntity<ApiResponse<LeaveRequestResponseDTO>> getLeaveRequest(@PathVariable UUID id) {
        LeaveRequestResponseDTO response = leaveRequestService.getLeaveRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Leave request retrieved successfully", response));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user's leave requests with optional status filter and pagination")
    public ResponseEntity<ApiResponse<PaginatedLeaveRequestResponse>> getCurrentUserLeaveRequests(
            @RequestParam(required = false) LeaveStatus status,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int pageNo,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int pageSize) {
        PaginatedLeaveRequestResponse response = leaveRequestService.getCurrentUserLeaveRequests(status, pageNo, pageSize);
        return ResponseEntity.ok(ApiResponse.success("Current user's leave requests retrieved successfully", response));
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending leave requests")
    public ResponseEntity<ApiResponse<List<LeaveRequestResponseDTO>>> getPendingLeaveRequests() {
        List<LeaveRequestResponseDTO> response = leaveRequestService.getPendingLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Pending leave requests retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update leave request status")
    public ResponseEntity<ApiResponse<LeaveRequestResponseDTO>> updateLeaveRequestStatus(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestUpdateDTO request) {
        LeaveRequestResponseDTO response = leaveRequestService.updateLeaveRequestStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Leave request status updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete leave request")
    public ResponseEntity<ApiResponse<Void>> deleteLeaveRequest(@PathVariable UUID id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Leave request deleted successfully", null));
    }
} 