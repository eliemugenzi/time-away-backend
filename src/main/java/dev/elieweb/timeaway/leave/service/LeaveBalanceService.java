package dev.elieweb.timeaway.leave.service;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.auth.service.CurrentUserService;
import dev.elieweb.timeaway.common.exception.BadRequestException;
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

import java.math.BigDecimal;
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

    private static final int MAX_CARRY_FORWARD_DAYS = 5;
    private static final BigDecimal DEFAULT_MONTHLY_ACCRUAL_RATE = new BigDecimal("1.66");

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
                .orElseThrow(() -> new BadRequestException("Leave balance not found"));

        User currentUser = currentUserService.getCurrentUser();
        if (!canAccessLeaveBalance(currentUser, leaveBalance)) {
            throw new BadRequestException("You don't have permission to view this leave balance");
        }

        return mapToResponseDTO(leaveBalance);
    }

    @Transactional
    public LeaveBalanceResponseDTO createLeaveBalance(LeaveBalanceDTO request) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() != UserRole.ROLE_ADMIN && 
            currentUser.getRole() != UserRole.ROLE_HR) {
            throw new BadRequestException("Only administrators and HR personnel can create leave balances");
        }

        LeaveBalance leaveBalance = LeaveBalance.builder()
                .user(request.getUser())
                .type(request.getType())
                .year(request.getYear())
                .totalDays(request.getTotalDays())
                .usedDays(0)
                .remainingDays(request.getTotalDays())
                .monthlyAccrualRate(request.getType() == LeaveType.ANNUAL ? 
                    DEFAULT_MONTHLY_ACCRUAL_RATE : BigDecimal.ZERO)
                .carriedForwardDays(0)
                .lastAccrualDate(LocalDate.now())
                .build();

        leaveBalance = leaveBalanceRepository.save(leaveBalance);
        return mapToResponseDTO(leaveBalance);
    }

    @Transactional
    public void updateLeaveBalance(LeaveRequest leaveRequest) {
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByUserAndTypeAndYear(leaveRequest.getUser(), leaveRequest.getType(), LocalDate.now().getYear())
                .orElseThrow(() -> new BadRequestException("Leave balance not found for the specified leave type"));

        long daysRequested = calculateWorkingDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
        if (leaveBalance.getRemainingDays() < daysRequested) {
            throw new BadRequestException(String.format(
                "Insufficient leave balance. You requested %d days but only have %d %s days remaining for %d.",
                daysRequested,
                leaveBalance.getRemainingDays(),
                leaveRequest.getType().toString().toLowerCase(),
                leaveBalance.getYear()
            ));
        }

        leaveBalance.setUsedDays(leaveBalance.getUsedDays() + (int) daysRequested);
        leaveBalance.setRemainingDays(leaveBalance.getRemainingDays() - (int) daysRequested);
        leaveBalanceRepository.save(leaveBalance);
    }

    public void checkLeaveBalance(User user, LeaveType type, LocalDate startDate, LocalDate endDate) {
        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByUserAndTypeAndYear(user, type, LocalDate.now().getYear())
                .orElseThrow(() -> new BadRequestException("Leave balance not found for the specified leave type"));

        long daysRequested = calculateWorkingDays(startDate, endDate);
        if (leaveBalance.getRemainingDays() < daysRequested) {
            throw new BadRequestException(String.format(
                "Insufficient leave balance. You requested %d days but only have %d %s days remaining for %d.",
                daysRequested,
                leaveBalance.getRemainingDays(),
                type.toString().toLowerCase(),
                leaveBalance.getYear()
            ));
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
                    .monthlyAccrualRate(DEFAULT_MONTHLY_ACCRUAL_RATE)
                    .carriedForwardDays(0)
                    .lastAccrualDate(LocalDate.now())
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
                    .monthlyAccrualRate(BigDecimal.ZERO)
                    .carriedForwardDays(0)
                    .lastAccrualDate(LocalDate.now())
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
                    .monthlyAccrualRate(BigDecimal.ZERO)
                    .carriedForwardDays(0)
                    .lastAccrualDate(LocalDate.now())
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
                .monthlyAccrualRate(leaveBalance.getMonthlyAccrualRate())
                .carriedForwardDays(leaveBalance.getCarriedForwardDays())
                .lastAccrualDate(leaveBalance.getLastAccrualDate())
                .createdAt(leaveBalance.getCreatedAt())
                .updatedAt(leaveBalance.getUpdatedAt())
                .build();
    }

    @Scheduled(cron = "0 0 0 1 * *") // Run at midnight on the first day of every month
    @Transactional
    public void processMonthlyAccrual() {
        LocalDate today = LocalDate.now();
        List<LeaveBalance> annualLeaveBalances = leaveBalanceRepository.findAll().stream()
                .filter(balance -> balance.getType() == LeaveType.ANNUAL)
                .collect(Collectors.toList());

        for (LeaveBalance balance : annualLeaveBalances) {
            if (balance.getLastAccrualDate() == null) {
                balance.setLastAccrualDate(today);
                continue;
            }

            // Calculate months since last accrual
            long monthsSinceLastAccrual = ChronoUnit.MONTHS.between(
                balance.getLastAccrualDate(),
                today
            );

            if (monthsSinceLastAccrual > 0) {
                // Calculate accrual amount
                BigDecimal accrualAmount = balance.getMonthlyAccrualRate()
                    .multiply(new BigDecimal(monthsSinceLastAccrual));
                
                // Update balance
                balance.setTotalDays(balance.getTotalDays() + accrualAmount.intValue());
                balance.setRemainingDays(balance.getRemainingDays() + accrualAmount.intValue());
                balance.setLastAccrualDate(today);
                
                leaveBalanceRepository.save(balance);
            }
        }
    }

    @Scheduled(cron = "0 0 0 1 1 *") // Run at midnight on January 1st
    @Transactional
    public void processYearEndCarryForward() {
        int oldYear = LocalDate.now().minusYears(1).getYear();
        int newYear = LocalDate.now().getYear();
        
        List<User> allUsers = currentUserService.findAllUsers(Sort.by(Sort.Direction.DESC, "createdAt"));
        
        for (User user : allUsers) {
            // Process annual leave type only
            LeaveBalance oldBalance = leaveBalanceRepository
                .findByUserAndTypeAndYear(user, LeaveType.ANNUAL, oldYear)
                .orElse(null);

            if (oldBalance != null) {
                // Calculate days to carry forward (max 5)
                int daysToCarryForward = Math.min(
                    oldBalance.getRemainingDays(),
                    MAX_CARRY_FORWARD_DAYS
                );

                // Create new year's balance
                LeaveBalance newBalance = LeaveBalance.builder()
                    .user(user)
                    .type(LeaveType.ANNUAL)
                    .year(newYear)
                    .totalDays(getDefaultDays(LeaveType.ANNUAL) + daysToCarryForward)
                    .usedDays(0)
                    .remainingDays(getDefaultDays(LeaveType.ANNUAL) + daysToCarryForward)
                    .monthlyAccrualRate(DEFAULT_MONTHLY_ACCRUAL_RATE)
                    .carriedForwardDays(daysToCarryForward)
                    .lastAccrualDate(LocalDate.now())
                    .build();

                leaveBalanceRepository.save(newBalance);
            }
        }
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
            case COMPASSIONATE -> 5;
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
                .orElseThrow(() -> new BadRequestException(
                    String.format("Leave balance not found for %s leave type in year %d", 
                        type.toString().toLowerCase(), 
                        year)
                ));
    }
} 