package dev.elieweb.timeaway.leave.controller;

import dev.elieweb.timeaway.common.dto.ApiResponse;
import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestResponseDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestUpdateDTO;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<ApiResponse> createLeaveRequest(@Valid @RequestBody LeaveRequestDTO request) {
        LeaveRequestResponseDTO response = leaveRequestService.createLeaveRequest(request);
        return ResponseEntity.ok(ApiResponse.success("Leave request created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllLeaveRequests() {
        List<LeaveRequestResponseDTO> requests = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Leave requests retrieved successfully", requests));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getLeaveRequest(@PathVariable UUID id) {
        LeaveRequestResponseDTO request = leaveRequestService.getLeaveRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Leave request retrieved successfully", request));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approveLeaveRequest(@PathVariable UUID id) {
        LeaveRequestUpdateDTO updateDTO = new LeaveRequestUpdateDTO();
        updateDTO.setStatus(LeaveStatus.APPROVED);
        LeaveRequestResponseDTO response = leaveRequestService.updateLeaveRequestStatus(id, updateDTO);
        return ResponseEntity.ok(ApiResponse.success("Leave request approved successfully", response));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse> rejectLeaveRequest(@PathVariable UUID id, @RequestBody(required = false) String rejectionReason) {
        LeaveRequestUpdateDTO updateDTO = new LeaveRequestUpdateDTO();
        updateDTO.setStatus(LeaveStatus.REJECTED);
        updateDTO.setRejectionReason(rejectionReason);
        LeaveRequestResponseDTO response = leaveRequestService.updateLeaveRequestStatus(id, updateDTO);
        return ResponseEntity.ok(ApiResponse.success("Leave request rejected successfully", response));
    }

    @GetMapping("/my-requests")
    public ResponseEntity<ApiResponse> getMyLeaveRequests() {
        List<LeaveRequestResponseDTO> requests = leaveRequestService.getCurrentUserLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Your leave requests retrieved successfully", requests));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateLeaveRequestStatus(
            @PathVariable UUID id,
            @Valid @RequestBody LeaveRequestUpdateDTO request
    ) {
        LeaveRequestResponseDTO response = leaveRequestService.updateLeaveRequestStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success("Leave request status updated successfully", response));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse> getPendingLeaveRequests() {
        List<LeaveRequestResponseDTO> requests = leaveRequestService.getPendingLeaveRequests();
        return ResponseEntity.ok(ApiResponse.success("Pending leave requests retrieved successfully", requests));
    }
} 