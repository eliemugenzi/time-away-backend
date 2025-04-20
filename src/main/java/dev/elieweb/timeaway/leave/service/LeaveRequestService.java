package dev.elieweb.timeaway.leave.service;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.service.CurrentUserService;
import dev.elieweb.timeaway.common.exception.BadRequestException;
import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestResponseDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestUpdateDTO;
import dev.elieweb.timeaway.leave.entity.LeaveBalance;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceService leaveBalanceService;
    private final CurrentUserService currentUserService;

    @Transactional
    public LeaveRequestResponseDTO createLeaveRequest(LeaveRequestDTO request) {
        User currentUser = currentUserService.getCurrentUser();
        
        // Validate dates
        LocalDate today = LocalDate.now();
        if (request.getStartDate().isBefore(today)) {
            throw new BadRequestException("Start date cannot be in the past");
        }
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BadRequestException("End date must be after start date");
        }
        
        // Check if user has any pending leave requests
        if (leaveRequestRepository.existsByUserAndStatus(currentUser, LeaveStatus.PENDING)) {
            throw new BadRequestException("You already have a pending leave request. Please wait for it to be processed before submitting a new one.");
        }

        // Validate leave balance
        LeaveBalance balance = leaveBalanceService.getLeaveBalance(currentUser, request.getType(), LocalDate.now().getYear());
        if (balance == null) {
            throw new BadRequestException("No leave balance found for the specified leave type");
        }

        // Calculate requested days (excluding weekends)
        long requestedDays = calculateWorkingDays(request.getStartDate(), request.getEndDate());

        // Check if user has enough days
        if (balance.getRemainingDays() < requestedDays) {
            throw new BadRequestException("Insufficient leave balance. You have " + balance.getRemainingDays() + " days remaining.");
        }

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

    public List<LeaveRequestResponseDTO> getAllLeaveRequests(LeaveStatus status) {
        User currentUser = currentUserService.getCurrentUser();
        List<LeaveRequest> requests;

        if (currentUser.getRole() == UserRole.ROLE_ADMIN || 
            currentUser.getRole() == UserRole.ROLE_MANAGER || 
            currentUser.getRole() == UserRole.ROLE_HR) {
            if (status != null) {
                requests = leaveRequestRepository.findByStatusOrderByCreatedAtDesc(status);
            } else {
                requests = leaveRequestRepository.findAllByOrderByCreatedAtDesc();
            }
        } else {
            if (status != null) {
                requests = leaveRequestRepository.findByUserAndStatusOrderByCreatedAtDesc(currentUser, status);
            } else {
                requests = leaveRequestRepository.findByUserOrderByCreatedAtDesc(currentUser);
            }
        }

        return requests.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestResponseDTO> getCurrentUserLeaveRequests(LeaveStatus status) {
        User currentUser = currentUserService.getCurrentUser();
        List<LeaveRequest> requests;
        
        if (status != null) {
            requests = leaveRequestRepository.findByUserAndStatusOrderByCreatedAtDesc(currentUser, status);
        } else {
            requests = leaveRequestRepository.findByUserOrderByCreatedAtDesc(currentUser);
        }
        
        return requests.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveRequestResponseDTO> getPendingLeaveRequests() {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN && 
            currentUser.getRole() != UserRole.ROLE_MANAGER && 
            currentUser.getRole() != UserRole.ROLE_HR) {
            throw new RuntimeException("You don't have permission to view pending leave requests");
        }
        return leaveRequestRepository.findByStatusOrderByCreatedAtDesc(LeaveStatus.PENDING).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public LeaveRequestResponseDTO getLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        User currentUser = currentUserService.getCurrentUser();
        if (!canAccessLeaveRequest(currentUser, leaveRequest)) {
            throw new RuntimeException("You don't have permission to view this leave request");
        }

        return mapToResponseDTO(leaveRequest);
    }

    @Transactional
    public LeaveRequestResponseDTO updateLeaveRequestStatus(UUID id, LeaveRequestUpdateDTO request) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        User currentUser = currentUserService.getCurrentUser();
        if (!canUpdateLeaveRequest(currentUser, leaveRequest)) {
            throw new RuntimeException("You don't have permission to update this leave request. Only HR, managers, and admins can update leave requests.");
        }

        // Set the approver when the request is being approved or rejected
        if (request.getStatus() == LeaveStatus.APPROVED || request.getStatus() == LeaveStatus.REJECTED) {
            leaveRequest.setApprover(currentUser);
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

        User currentUser = currentUserService.getCurrentUser();
        if (!canDeleteLeaveRequest(currentUser, leaveRequest)) {
            throw new RuntimeException("You don't have permission to delete this leave request");
        }

        leaveRequestRepository.delete(leaveRequest);
    }

    private boolean canAccessLeaveRequest(User user, LeaveRequest leaveRequest) {
        return user.getRole() == UserRole.ROLE_ADMIN || 
               user.getRole() == UserRole.ROLE_MANAGER || 
               user.getRole() == UserRole.ROLE_HR ||
               leaveRequest.getUser().getId().equals(user.getId());
    }

    private boolean canUpdateLeaveRequest(User user, LeaveRequest leaveRequest) {
        return user.getRole() == UserRole.ROLE_ADMIN || 
               user.getRole() == UserRole.ROLE_MANAGER ||
               user.getRole() == UserRole.ROLE_HR;
    }

    private boolean canDeleteLeaveRequest(User user, LeaveRequest leaveRequest) {
        return user.getRole() == UserRole.ROLE_ADMIN || 
               leaveRequest.getUser().getId().equals(user.getId());
    }

    private long calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        long days = 0;
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            if (currentDate.getDayOfWeek().getValue() < 6) { // Monday = 1, Friday = 5
                days++;
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return days;
    }

    private LeaveRequestResponseDTO mapToResponseDTO(LeaveRequest leaveRequest) {
        LeaveRequestResponseDTO.ApproverDTO approverDTO = null;
        if (leaveRequest.getApprover() != null) {
            User approver = leaveRequest.getApprover();
            approverDTO = LeaveRequestResponseDTO.ApproverDTO.builder()
                    .id(approver.getId())
                    .firstName(approver.getFirstName())
                    .lastName(approver.getLastName())
                    .email(approver.getEmail())
                    .build();
        }

        return LeaveRequestResponseDTO.builder()
                .id(leaveRequest.getId())
                .employeeName(leaveRequest.getUser().getFirstName() + " " + leaveRequest.getUser().getLastName())
                .type(leaveRequest.getType())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .rejectionReason(leaveRequest.getRejectionReason())
                .approver(approverDTO)
                .createdAt(leaveRequest.getCreatedAt())
                .updatedAt(leaveRequest.getUpdatedAt())
                .build();
    }
} 