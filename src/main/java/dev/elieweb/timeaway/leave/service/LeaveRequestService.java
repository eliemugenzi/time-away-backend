package dev.elieweb.timeaway.leave.service;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.service.CurrentUserService;
import dev.elieweb.timeaway.common.exception.BadRequestException;
import dev.elieweb.timeaway.common.service.FileStorageService;
import dev.elieweb.timeaway.email.service.EmailService;
import dev.elieweb.timeaway.leave.dto.LeaveRequestDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestResponseDTO;
import dev.elieweb.timeaway.leave.dto.LeaveRequestUpdateDTO;
import dev.elieweb.timeaway.leave.dto.PaginatedLeaveRequestResponse;
import dev.elieweb.timeaway.leave.dto.MonthlyLeaveStatisticsDTO;
import dev.elieweb.timeaway.leave.entity.LeaveBalance;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import dev.elieweb.timeaway.leave.enums.LeaveDurationType;
import dev.elieweb.timeaway.leave.repository.LeaveRequestRepository;
import dev.elieweb.timeaway.common.config.FileUrlConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceService leaveBalanceService;
    private final CurrentUserService currentUserService;
    private final FileStorageService fileStorageService;
    private final FileUrlConfig fileUrlConfig;
    private final EmailService emailService;

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

        // Validate duration type
        if (!request.getStartDate().isEqual(request.getEndDate()) && 
            request.getDurationType() != LeaveDurationType.FULL_DAY) {
            throw new BadRequestException("Half-day option is only available when start date equals end date");
        }
        
        // Check if user has any pending leave requests
        if (leaveRequestRepository.existsByUserAndStatus(currentUser, LeaveStatus.PENDING)) {
            throw new BadRequestException("You already have a pending leave request. Please wait for it to be processed before submitting a new one.");
        }

        // Check for overlapping approved leave requests
        if (leaveRequestRepository.hasOverlappingApprovedLeaveRequest(currentUser, request.getStartDate(), request.getEndDate())) {
            System.out.println("Overlapping approved leave request found");
            throw new BadRequestException("You already have an approved leave request that overlaps with these dates.");
        }

        // Validate leave balance
        LeaveBalance balance = leaveBalanceService.getLeaveBalance(currentUser, request.getType(), LocalDate.now().getYear());
        if (balance == null) {
            throw new BadRequestException("No leave balance found for the specified leave type");
        }

        // Calculate requested days (excluding weekends)
        long requestedDays = calculateWorkingDays(request.getStartDate(), request.getEndDate());
        if (request.getDurationType() != LeaveDurationType.FULL_DAY) {
            requestedDays = requestedDays / 2;
        }

        // Check if user has enough days
        if (balance.getRemainingDays() < requestedDays) {
            throw new BadRequestException("Insufficient leave balance. You have " + balance.getRemainingDays() + " days remaining.");
        }

        // Handle file upload
        String supportingDocumentUrl = null;
        String supportingDocumentName = null;
        if (request.getSupportingDocument() != null && !request.getSupportingDocument().isEmpty()) {
            supportingDocumentName = request.getSupportingDocument().getOriginalFilename();
            supportingDocumentUrl = fileStorageService.storeFile(request.getSupportingDocument());
        }

        LeaveRequest leaveRequest = LeaveRequest.builder()
                .user(currentUser)
                .department(currentUser.getDepartment())
                .type(request.getType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .status(LeaveStatus.PENDING)
                .durationType(request.getDurationType())
                .supportingDocumentUrl(supportingDocumentUrl)
                .supportingDocumentName(supportingDocumentName)
                .build();

        leaveRequest = leaveRequestRepository.save(leaveRequest);
        
        // Send email notification
        emailService.sendLeaveRequestSubmittedEmail(leaveRequest);
        
        return mapToResponseDTO(leaveRequest);
    }

    public PaginatedLeaveRequestResponse getAllLeaveRequests(LeaveStatus status, int pageNo, int pageSize) {
        User currentUser = currentUserService.getCurrentUser();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<LeaveRequest> page;

        if (currentUser.getRole() == UserRole.ROLE_ADMIN || 
            currentUser.getRole() == UserRole.ROLE_MANAGER || 
            currentUser.getRole() == UserRole.ROLE_HR) {
            if (status != null) {
                page = leaveRequestRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
            } else {
                page = leaveRequestRepository.findAllByOrderByCreatedAtDesc(pageable);
            }
        } else {
            if (status != null) {
                page = leaveRequestRepository.findByUserAndStatusOrderByCreatedAtDesc(currentUser, status, pageable);
            } else {
                page = leaveRequestRepository.findByUserOrderByCreatedAtDesc(currentUser, pageable);
            }
        }

        List<LeaveRequestResponseDTO> content = page.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return PaginatedLeaveRequestResponse.builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public PaginatedLeaveRequestResponse getCurrentUserLeaveRequests(LeaveStatus status, int pageNo, int pageSize) {
        User currentUser = currentUserService.getCurrentUser();
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<LeaveRequest> page;
        
        if (status != null) {
            page = leaveRequestRepository.findByUserAndStatusOrderByCreatedAtDesc(currentUser, status, pageable);
        } else {
            page = leaveRequestRepository.findByUserOrderByCreatedAtDesc(currentUser, pageable);
        }
        
        List<LeaveRequestResponseDTO> content = page.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return PaginatedLeaveRequestResponse.builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public List<LeaveRequestResponseDTO> getPendingLeaveRequests() {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN && 
            currentUser.getRole() != UserRole.ROLE_MANAGER && 
            currentUser.getRole() != UserRole.ROLE_HR) {
            throw new BadRequestException("You don't have permission to view pending leave requests");
        }
        return leaveRequestRepository.findByStatusOrderByCreatedAtDesc(LeaveStatus.PENDING).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public LeaveRequestResponseDTO getLeaveRequest(UUID id) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Leave request not found"));

        User currentUser = currentUserService.getCurrentUser();
        if (!canAccessLeaveRequest(currentUser, leaveRequest)) {
            throw new BadRequestException("You don't have permission to view this leave request");
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

        // Send email notification
        emailService.sendLeaveRequestStatusUpdateEmail(leaveRequest);

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

        // Delete associated file if exists
        if (leaveRequest.getSupportingDocumentUrl() != null) {
            fileStorageService.deleteFile(leaveRequest.getSupportingDocumentUrl());
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

        String supportingDocumentUrl = leaveRequest.getSupportingDocumentUrl() != null ?
                fileUrlConfig.getFileUrl(leaveRequest.getSupportingDocumentUrl()) : null;

        return LeaveRequestResponseDTO.builder()
                .id(leaveRequest.getId())
                .employeeName(leaveRequest.getUser().getFirstName() + " " + leaveRequest.getUser().getLastName())
                .departmentId(leaveRequest.getDepartment().getId())
                .type(leaveRequest.getType())
                .startDate(leaveRequest.getStartDate())
                .endDate(leaveRequest.getEndDate())
                .reason(leaveRequest.getReason())
                .status(leaveRequest.getStatus())
                .rejectionReason(leaveRequest.getRejectionReason())
                .approver(approverDTO)
                .supportingDocumentUrl(supportingDocumentUrl)
                .supportingDocumentName(leaveRequest.getSupportingDocumentName())
                .createdAt(leaveRequest.getCreatedAt())
                .updatedAt(leaveRequest.getUpdatedAt())
                .build();
    }

    public PaginatedLeaveRequestResponse searchLeaveRequests(String employeeName, LeaveStatus status, UUID departmentId, int pageNo, int pageSize) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN && 
            currentUser.getRole() != UserRole.ROLE_HR) {
            throw new RuntimeException("Only admins and HR can search leave requests by employee name");
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<LeaveRequest> page;

        if (employeeName != null && !employeeName.trim().isEmpty()) {
            if (status != null) {
                page = leaveRequestRepository.searchByEmployeeNameAndStatusAndDepartment(employeeName, status, departmentId, pageable);
            } else {
                page = leaveRequestRepository.searchByEmployeeNameAndDepartment(employeeName, departmentId, pageable);
            }
        } else {
            page = leaveRequestRepository.findByStatusAndDepartment(status, departmentId, pageable);
        }

        List<LeaveRequestResponseDTO> content = page.getContent().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        return PaginatedLeaveRequestResponse.builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    public List<MonthlyLeaveStatisticsDTO> getMonthlyApprovedLeaves(int year) {
        List<LeaveRequest> approvedLeaves = leaveRequestRepository.findByStatusAndStartDateBetween(
            LeaveStatus.APPROVED,
            LocalDate.of(year, 1, 1),
            LocalDate.of(year, 12, 31)
        );

        Map<Integer, MonthlyLeaveStatisticsDTO> monthlyStats = new HashMap<>();
        
        // Initialize all months with zero values
        for (int month = 1; month <= 12; month++) {
            LocalDate date = LocalDate.of(year, month, 1);
            Map<LeaveType, MonthlyLeaveStatisticsDTO.LeaveTypeStats> leaveTypeStats = new HashMap<>();
            
            // Initialize stats for all leave types
            for (LeaveType type : LeaveType.values()) {
                leaveTypeStats.put(type, new MonthlyLeaveStatisticsDTO.LeaveTypeStats());
            }
            
            monthlyStats.put(month, MonthlyLeaveStatisticsDTO.builder()
                .year(year)
                .month(month)
                .monthName(date.getMonth().toString())
                .approvedLeaveCount(0)
                .totalLeaveDays(0.0)
                .leaveTypeStatistics(leaveTypeStats)
                .build());
        }

        // Update months that have approved leaves
        approvedLeaves.forEach(leave -> {
            int month = leave.getStartDate().getMonthValue();
            MonthlyLeaveStatisticsDTO monthStats = monthlyStats.get(month);
            double duration = leave.getDuration();
            
            // Update overall statistics
            monthStats.setApprovedLeaveCount(monthStats.getApprovedLeaveCount() + 1);
            monthStats.setTotalLeaveDays(monthStats.getTotalLeaveDays() + duration);
            
            // Update leave type specific statistics
            MonthlyLeaveStatisticsDTO.LeaveTypeStats typeStats = monthStats.getLeaveTypeStatistics().get(leave.getType());
            typeStats.incrementCount();
            typeStats.addDays(duration);
        });

        // Return sorted list by month
        return monthlyStats.values().stream()
            .sorted(Comparator.comparingInt(MonthlyLeaveStatisticsDTO::getMonth))
            .collect(Collectors.toList());
    }
} 