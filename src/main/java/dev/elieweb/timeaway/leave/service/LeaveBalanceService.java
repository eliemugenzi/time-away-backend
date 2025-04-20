package dev.elieweb.timeaway.leave.service;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.service.CurrentUserService;
import dev.elieweb.timeaway.leave.dto.LeaveBalanceDTO;
import dev.elieweb.timeaway.leave.dto.LeaveBalanceInfo;
import dev.elieweb.timeaway.leave.dto.LeaveBalanceResponseDTO;
import dev.elieweb.timeaway.leave.entity.LeaveBalance;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import dev.elieweb.timeaway.leave.repository.LeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final CurrentUserService currentUserService;

    public List<LeaveBalanceResponseDTO> getAllLeaveBalances() {
        User currentUser = currentUserService.getCurrentUser();
        List<LeaveBalance> balances;

        if (currentUser.getRole() == UserRole.ROLE_ADMIN || 
            currentUser.getRole() == UserRole.ROLE_MANAGER || 
            currentUser.getRole() == UserRole.ROLE_HR) {
            balances = leaveBalanceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else {
            balances = leaveBalanceRepository.findByUserOrderByCreatedAtDesc(currentUser);
        }

        return balances.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<LeaveBalanceResponseDTO> getCurrentUserLeaveBalances() {
        User currentUser = currentUserService.getCurrentUser();
        return leaveBalanceRepository.findByUserOrderByCreatedAtDesc(currentUser).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public LeaveBalanceResponseDTO getLeaveBalance(Long id) {
        LeaveBalance leaveBalance = leaveBalanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        User currentUser = currentUserService.getCurrentUser();
        if (!canAccessLeaveBalance(currentUser, leaveBalance)) {
            throw new RuntimeException("You don't have permission to view this leave balance");
        }

        return mapToResponseDTO(leaveBalance);
    }

    @Transactional
    public LeaveBalanceResponseDTO createLeaveBalance(LeaveBalanceDTO request) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN && 
            currentUser.getRole() != UserRole.ROLE_HR) {
            throw new RuntimeException("Only admins and HR can create leave balances");
        }

        LeaveBalance leaveBalance = LeaveBalance.builder()
                .user(request.getUser())
                .type(request.getType())
                .year(request.getYear())
                .totalDays(request.getTotalDays())
                .usedDays(0)
                .remainingDays(request.getTotalDays())
                .build();

        leaveBalance = leaveBalanceRepository.save(leaveBalance);
        return mapToResponseDTO(leaveBalance);
    }

    @Transactional
    public void updateLeaveBalance(LeaveRequest leaveRequest) {
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByUserAndTypeAndYear(leaveRequest.getUser(), leaveRequest.getType(), LocalDate.now().getYear())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        long daysRequested = calculateWorkingDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
        if (leaveBalance.getRemainingDays() < daysRequested) {
            throw new RuntimeException("Insufficient leave balance");
        }

        leaveBalance.setUsedDays(leaveBalance.getUsedDays() + (int) daysRequested);
        leaveBalance.setRemainingDays(leaveBalance.getRemainingDays() - (int) daysRequested);
        leaveBalanceRepository.save(leaveBalance);
    }

    public void checkLeaveBalance(User user, LeaveType type, LocalDate startDate, LocalDate endDate) {
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByUserAndTypeAndYear(user, type, LocalDate.now().getYear())
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        long daysRequested = calculateWorkingDays(startDate, endDate);
        if (leaveBalance.getRemainingDays() < daysRequested) {
            throw new RuntimeException("Insufficient leave balance");
        }
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

    public void initializeLeaveBalance(User user) {
        int currentYear = LocalDate.now().getYear();

        // Initialize Annual Leave
        if (!leaveBalanceRepository.existsByUserAndTypeAndYear(user, LeaveType.ANNUAL, currentYear)) {
            LeaveBalance annualLeave = LeaveBalance.builder()
                    .user(user)
                    .type(LeaveType.ANNUAL)
                    .year(currentYear)
                    .totalDays(21)
                    .usedDays(0)
                    .remainingDays(21)
                    .build();
            leaveBalanceRepository.save(annualLeave);
        }

        // Initialize Sick Leave
        if (!leaveBalanceRepository.existsByUserAndTypeAndYear(user, LeaveType.SICK, currentYear)) {
            LeaveBalance sickLeave = LeaveBalance.builder()
                    .user(user)
                    .type(LeaveType.SICK)
                    .year(currentYear)
                    .totalDays(10)
                    .usedDays(0)
                    .remainingDays(10)
                    .build();
            leaveBalanceRepository.save(sickLeave);
        }

        // Initialize Maternity Leave
        if (!leaveBalanceRepository.existsByUserAndTypeAndYear(user, LeaveType.MATERNITY, currentYear)) {
            LeaveBalance maternityLeave = LeaveBalance.builder()
                    .user(user)
                    .type(LeaveType.MATERNITY)
                    .year(currentYear)
                    .totalDays(90)
                    .usedDays(0)
                    .remainingDays(90)
                    .build();
            leaveBalanceRepository.save(maternityLeave);
        }
    }

    private boolean canAccessLeaveBalance(User user, LeaveBalance leaveBalance) {
        return user.getRole() == UserRole.ROLE_ADMIN || 
               user.getRole() == UserRole.ROLE_MANAGER || 
               user.getRole() == UserRole.ROLE_HR ||
               leaveBalance.getUser().getId().equals(user.getId());
    }

    private LeaveBalanceResponseDTO mapToResponseDTO(LeaveBalance leaveBalance) {
        return LeaveBalanceResponseDTO.builder()
                .id(leaveBalance.getId())
                .userId(leaveBalance.getUser().getId())
                .userName(leaveBalance.getUser().getFirstName() + " " + leaveBalance.getUser().getLastName())
                .type(leaveBalance.getType())
                .year(leaveBalance.getYear())
                .totalDays(leaveBalance.getTotalDays())
                .usedDays(leaveBalance.getUsedDays())
                .remainingDays(leaveBalance.getRemainingDays())
                .createdAt(leaveBalance.getCreatedAt())
                .updatedAt(leaveBalance.getUpdatedAt())
                .build();
    }

    @Scheduled(cron = "0 0 0 1 1 *") // Run at midnight on January 1st
    @Transactional
    public void resetAnnualLeaveBalances() {
        int newYear = LocalDate.now().getYear();
        List<User> allUsers = currentUserService.findAllUsers(Sort.by(Sort.Direction.DESC, "createdAt"));
        
        for (User user : allUsers) {
            for (LeaveType leaveType : LeaveType.values()) {
                if (getDefaultDays(leaveType) > 0) {
                    LeaveBalance balance = LeaveBalance.builder()
                            .user(user)
                            .type(leaveType)
                            .year(newYear)
                            .totalDays(getDefaultDays(leaveType))
                            .usedDays(0)
                            .remainingDays(getDefaultDays(leaveType))
                            .build();
                    leaveBalanceRepository.save(balance);
                }
            }
        }
    }

    @Transactional
    public void initializeLeaveBalancesForExistingUsers() {
        int currentYear = LocalDate.now().getYear();
        List<User> allUsers = currentUserService.findAllUsers(Sort.by(Sort.Direction.DESC, "createdAt"));
        
        for (User user : allUsers) {
            // Check if user already has leave balances for the current year
            List<LeaveBalance> existingBalances = leaveBalanceRepository.findByUserAndYearOrderByCreatedAtDesc(user, currentYear);
            if (existingBalances.isEmpty()) {
                initializeLeaveBalance(user);
            }
        }
    }

    private int getDefaultDays(LeaveType type) {
        return switch (type) {
            case ANNUAL -> 20;
            case SICK -> 10;
            case MATERNITY -> 90;
            case PATERNITY -> 14;
            case BEREAVEMENT -> 5;
            case UNPAID -> 0;
        };
    }

    public Map<LeaveType, LeaveBalanceInfo> getCurrentBalances(User user) {
        int currentYear = LocalDate.now().getYear();
        List<LeaveBalance> balances = leaveBalanceRepository.findByUserAndYearOrderByCreatedAtDesc(user, currentYear);
        
        return balances.stream()
                .collect(Collectors.toMap(
                    LeaveBalance::getType,
                    balance -> LeaveBalanceInfo.builder()
                        .totalDays(balance.getTotalDays())
                        .usedDays(balance.getUsedDays())
                        .remainingDays(balance.getRemainingDays())
                        .build()
                ));
    }

    public LeaveBalance getLeaveBalance(User user, LeaveType type, int year) {
        return leaveBalanceRepository
                .findByUserAndTypeAndYear(user, type, year)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));
    }
} 