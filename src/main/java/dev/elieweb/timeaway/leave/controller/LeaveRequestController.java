package dev.elieweb.timeaway.leave.controller;

import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestResponseDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestUpdateDTO;
import dev.elieweb.timeaway.leave.service.LeaveRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/leave-requests")
@RequiredArgsConstructor
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    @PostMapping
    public ResponseEntity<LeaveRequestResponseDTO> createLeaveRequest(
            @RequestBody @Valid LeaveRequestDTO request
    ) {
        return ResponseEntity.ok(leaveRequestService.createLeaveRequest(request));
    }

    @PutMapping("/{requestId}")
    public ResponseEntity<LeaveRequestResponseDTO> updateLeaveRequestStatus(
            @PathVariable Long requestId,
            @RequestBody @Valid LeaveRequestUpdateDTO request
    ) {
        return ResponseEntity.ok(leaveRequestService.updateLeaveRequestStatus(requestId, request));
    }

    @GetMapping("/me")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getCurrentUserLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getCurrentUserLeaveRequests());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getPendingLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getPendingLeaveRequests());
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<LeaveRequestResponseDTO> getLeaveRequest(
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequest(requestId));
    }
} 