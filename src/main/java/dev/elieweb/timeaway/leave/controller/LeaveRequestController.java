package dev.elieweb.timeaway.leave.controller;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestResponseDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestUpdateDTO;
import dev.elieweb.timeaway.leave.dto.PaginatedLeaveRequestResponse;
import dev.elieweb.timeaway.leave.dto.MonthlyLeaveStatisticsDTO;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import dev.elieweb.timeaway.leave.enums.LeaveDurationType;
import dev.elieweb.timeaway.leave.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leave-requests")
@RequiredArgsConstructor
@Tag(name = "Leave Request", description = "Leave Request Management APIs")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new leave request")
    public ResponseEntity<ApiResponse<LeaveRequestResponseDTO>> createLeaveRequest(
            @RequestParam LeaveType type,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam String reason,
            @RequestParam(defaultValue = "FULL_DAY") LeaveDurationType durationType,
            @RequestParam(required = false) MultipartFile supportingDocument) {
        
        LeaveRequestDTO request = new LeaveRequestDTO(type, startDate, endDate, reason, durationType, supportingDocument);
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

    @GetMapping("/search")
    @Operation(summary = "Search leave requests by employee name and status")
    public ResponseEntity<PaginatedLeaveRequestResponse> searchLeaveRequests(
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) LeaveStatus status,
            @RequestParam(required = false) UUID departmentId,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(leaveRequestService.searchLeaveRequests(employeeName, status, departmentId, pageNo, pageSize));
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

    @GetMapping("/monthly-statistics/{year}")
    @Operation(summary = "Get monthly approved leave statistics for a specific year")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<ApiResponse<List<MonthlyLeaveStatisticsDTO>>> getMonthlyApprovedLeaves(
            @Parameter(description = "Year to get statistics for", example = "2024")
            @PathVariable int year) {
        List<MonthlyLeaveStatisticsDTO> statistics = leaveRequestService.getMonthlyApprovedLeaves(year);
        return ResponseEntity.ok(ApiResponse.success("Monthly leave statistics retrieved successfully", statistics));
    }
} 