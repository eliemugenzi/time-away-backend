package dev.elieweb.timeaway.leave.controller;

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
    public ResponseEntity<LeaveRequestResponseDTO> createLeaveRequest(
            @Valid @RequestBody LeaveRequestDTO request
    ) {
        return ResponseEntity.ok(leaveRequestService.createLeaveRequest(request));
    }

    @GetMapping
    public ResponseEntity<List<LeaveRequestResponseDTO>> getAllLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaveRequests());
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<LeaveRequestResponseDTO>> getMyLeaveRequests() {
        return ResponseEntity.ok(leaveRequestService.getCurrentUserLeaveRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequestResponseDTO> getLeaveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getLeaveRequest(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<LeaveRequestResponseDTO> updateLeaveRequest(
            @PathVariable Long id,
            @Valid @RequestBody LeaveRequestUpdateDTO request
    ) {
        return ResponseEntity.ok(leaveRequestService.updateLeaveRequestStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaveRequest(@PathVariable Long id) {
        leaveRequestService.deleteLeaveRequest(id);
        return ResponseEntity.noContent().build();
    }
} 