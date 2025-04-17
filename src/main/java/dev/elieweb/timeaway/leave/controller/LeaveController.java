package dev.elieweb.timeaway.leave.controller;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestResponseDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestUpdateDTO;
import dev.elieweb.timeaway.leave.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leaves")
@RequiredArgsConstructor
public class LeaveController {
    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse> createLeaveRequest(
            @Valid @RequestBody LeaveRequestDTO request
    ) {
        LeaveRequestResponseDTO response = leaveRequestService.createLeaveRequest(request);
        return ResponseEntity.ok(ApiResponse.success("Leave request created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllLeaveRequests() {
        List<LeaveRequestResponseDTO> requests = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Leave requests retrieved successfully", requests));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<ApiResponse> getMyLeaveRequests() {
        List<LeaveRequestResponseDTO> requests = leaveRequestService.getCurrentUserLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Your leave requests retrieved successfully", requests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLeaveRequest(@PathVariable Long id) {
        LeaveRequestResponseDTO request = leaveRequestService.getLeaveRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Leave request retrieved successfully", request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse> updateLeaveRequest(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestUpdateDTO request
    ) {
        LeaveRequestResponseDTO response = leaveRequestService.updateLeaveRequestStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Leave request updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Leave request deleted successfully", null));
    }
} 