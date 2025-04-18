package dev.elieweb.timeaway.leave.service;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.repository.UserRepository;
import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestResponseDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestUpdateDTO;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveBalanceService leaveBalanceService;

    @Transactional
    public LeaveRequestResponseDTO createLeaveRequest(LeaveRequestDTO request) {
        User currentUser = getCurrentUser();
        
        // Check leave balance
        leaveBalanceService.checkLeaveBalance(currentUser, request.getType(), request.getStartDate(), request.getEndDate());

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .user(currentUser)
                .type(request.getType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .build();

        leaveRequest = leaveRequestRepository.save(leaveRequest);
        return mapToResponseDTO(leaveRequest);
    }

    public List<LeaveRequestResponseDTO> getAllLeaveRequests() {
        User currentUser = getCurrentUser();
        List<LeaveRequest> requests;

        if (currentUser.getRole() == UserRole.ROLE_ADMIN || currentUser.getRole() == UserRole.ROLE_MANAGER) {
            requests = leaveRequestRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            requests = leaveRequestRepository.findByUserOrderByCreatedAtDesc(currentUser);
        }

        return requests.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestResponseDTO> getCurrentUserLeaveRequests() {
        User currentUser = getCurrentUser();
        return leaveRequestRepository.findByUserOrderByCreatedAtDesc(currentUser).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestResponseDTO> getPendingLeaveRequests() {
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN && currentUser.getRole() != UserRole.ROLE_MANAGER) {
            throw new RuntimeException("You don't have permission to view pending leave requests");
        }
        return leaveRequestRepository.findByStatusOrderByCreatedAtDesc(LeaveStatus.PENDING).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public LeaveRequestResponseDTO getLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        User currentUser = getCurrentUser();
        if (!canAccessLeaveRequest(currentUser, leaveRequest)) {
            throw new RuntimeException("You don't have permission to view this leave request");
        }

        return mapToResponseDTO(leaveRequest);
    }

    @Transactional
    public LeaveRequestResponseDTO updateLeaveRequestStatus(UUID id, LeaveRequestUpdateDTO request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        User currentUser = getCurrentUser();
        if (!canUpdateLeaveRequest(currentUser, leaveRequest)) {
            throw new RuntimeException("You don't have permission to update this leave request");
        }

        leaveRequest.setStatus(request.getStatus());
        if (request.getRejectionReason() != null) {
            leaveRequest.setRejectionReason(request.getRejectionReason());
        }

        leaveRequest = leaveRequestRepository.save(leaveRequest);

        // Update leave balance if request is approved
        if (request.getStatus() == LeaveStatus.APPROVED) {
            leaveBalanceService.updateLeaveBalance(leaveRequest);
        }

        return mapToResponseDTO(leaveRequest);
    }

    @Transactional
    public void deleteLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        User currentUser = getCurrentUser();
        if (!canDeleteLeaveRequest(currentUser, leaveRequest)) {
            throw new RuntimeException("You don't have permission to delete this leave request");
        }

        leaveRequestRepository.delete(leaveRequest);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean canAccessLeaveRequest(User user, LeaveRequest leaveRequest) {
        return user.getRole() == UserRole.ROLE_ADMIN || 
               user.getRole() == UserRole.ROLE_MANAGER || 
               leaveRequest.getUser().getId().equals(user.getId());
    }

    private boolean canUpdateLeaveRequest(User user, LeaveRequest leaveRequest) {
        return user.getRole() == UserRole.ROLE_ADMIN || user.getRole() == UserRole.ROLE_MANAGER;
    }

    private boolean canDeleteLeaveRequest(User user, LeaveRequest leaveRequest) {
        return user.getRole() == UserRole.ROLE_ADMIN || 
               leaveRequest.getUser().getId().equals(user.getId());
    }

    private LeaveRequestResponseDTO mapToResponseDTO(LeaveRequest leaveRequest) {
        return LeaveRequestResponseDTO.builder()
                .id(leaveRequest.getId())
                .employeeName(leaveRequest.getUser().getFirstName() + " " + leaveRequest.getUser().getLastName())
                .type(leaveRequest.getType())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .rejectionReason(leaveRequest.getRejectionReason())
                .createdAt(leaveRequest.getCreatedAt())
                .updatedAt(leaveRequest.getUpdatedAt())
                .build();
    }
} 