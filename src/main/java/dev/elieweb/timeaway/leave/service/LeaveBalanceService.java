package dev.elieweb.timeaway.leave.service;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.repository.UserRepository;
import dev.elieweb.timeaway.leave.entity.LeaveBalance;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import dev.elieweb.timeaway.leave.repository.LeaveBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UserRepository userRepository;

    @Transactional
    public void checkLeaveBalance(User user, LeaveType type, LocalDate startDate, LocalDate endDate) {
        LeaveBalance balance = getOrCreateBalance(user, type);
        long requestedDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int availableDays = balance.getTotalDays() - balance.getUsedDays();

        if (availableDays < requestedDays) {
            throw new RuntimeException("Insufficient leave balance. Available days: " + availableDays + ", Requested days: " + requestedDays);
        }
    }

    @Transactional
    public void initializeLeaveBalance(User user) {
        int currentYear = LocalDate.now().getYear();

        for (LeaveType type : LeaveType.values()) {
            LeaveBalance balance = LeaveBalance.builder()
                    .user(user)
                    .type(type)
                    .totalDays(getDefaultDays(type))
                    .usedDays(0)
                    .year(currentYear)
                    .build();

            leaveBalanceRepository.save(balance);
        }
    }

    @Transactional
    public boolean hasEnoughBalance(User user, LeaveRequest leaveRequest) {
        LeaveBalance balance = getOrCreateBalance(user, leaveRequest.getType());
        long requestedDays = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
        return (balance.getTotalDays() - balance.getUsedDays()) >= requestedDays;
    }

    @Transactional
    public void updateLeaveBalance(LeaveRequest leaveRequest) {
        User user = leaveRequest.getUser();
        LeaveType type = leaveRequest.getType();
        int year = leaveRequest.getStartDate().getYear();

        LeaveBalance balance = leaveBalanceRepository.findByUserAndTypeAndYear(user, type, year)
                .orElseThrow(() -> new RuntimeException("Leave balance not found"));

        long days = ChronoUnit.DAYS.between(leaveRequest.getStartDate(), leaveRequest.getEndDate()) + 1;
        balance.setUsedDays(balance.getUsedDays() + (int) days);

        leaveBalanceRepository.save(balance);
    }

    public Map<LeaveType, Integer> getCurrentBalances(User user) {
        int currentYear = LocalDate.now().getYear();
        List<LeaveBalance> balances = leaveBalanceRepository.findByUserAndYearOrderByCreatedAtDesc(user, currentYear);
        
        return balances.stream()
                .collect(Collectors.toMap(
                        LeaveBalance::getType,
                        balance -> balance.getTotalDays() - balance.getUsedDays()
                ));
    }

    @Scheduled(cron = "0 0 0 1 1 *") // Run at midnight on January 1st
    @Transactional
    public void resetAnnualLeaveBalances() {
        int newYear = LocalDate.now().getYear();
        List<User> allUsers = userRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        
        for (User user : allUsers) {
            for (LeaveType leaveType : LeaveType.values()) {
                if (getDefaultDays(leaveType) > 0) {
                    LeaveBalance balance = LeaveBalance.builder()
                            .user(user)
                            .type(leaveType)
                            .totalDays(getDefaultDays(leaveType))
                            .usedDays(0)
                            .year(newYear)
                            .build();
                    leaveBalanceRepository.save(balance);
                }
            }
        }
    }

    private LeaveBalance getOrCreateBalance(User user, LeaveType leaveType) {
        int currentYear = LocalDate.now().getYear();
        return leaveBalanceRepository.findByUserAndTypeAndYear(user, leaveType, currentYear)
                .orElseGet(() -> {
                    LeaveBalance newBalance = LeaveBalance.builder()
                            .user(user)
                            .type(leaveType)
                            .totalDays(getDefaultDays(leaveType))
                            .usedDays(0)
                            .year(currentYear)
                            .build();
                    return leaveBalanceRepository.save(newBalance);
                });
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
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
} 