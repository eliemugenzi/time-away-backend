package dev.elieweb.timeaway.leave.service;

import dev.elieweb.timeaway.leave.dto.DepartmentStatistics;
import dev.elieweb.timeaway.leave.dto.LeaveStatistics;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import dev.elieweb.timeaway.leave.repository.LeaveRequestRepository;
import dev.elieweb.timeaway.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaveStatisticsService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    public LeaveStatistics getOverallStatistics() {
        return LeaveStatistics.builder()
                .totalRequests((int) leaveRequestRepository.count())
                .pendingRequests((int) leaveRequestRepository.countByStatus(LeaveStatus.PENDING))
                .approvedRequests((int) leaveRequestRepository.countByStatus(LeaveStatus.APPROVED))
                .rejectedRequests((int) leaveRequestRepository.countByStatus(LeaveStatus.REJECTED))
                .cancelledRequests((int) leaveRequestRepository.countByStatus(LeaveStatus.CANCELLED))
                .averageLeaveDuration(calculateAverageLeaveDuration())
                .build();
    }

    public List<DepartmentStatistics> getDepartmentStatistics() {
        return userRepository.findAllDepartments().stream()
                .map(this::calculateDepartmentStatistics)
                .collect(Collectors.toList());
    }

    private DepartmentStatistics calculateDepartmentStatistics(String department) {
        return DepartmentStatistics.builder()
                .department(department)
                .totalEmployees((int) userRepository.countByDepartment(department))
                .employeesOnLeave(countEmployeesOnLeave(department))
                .totalLeaveRequests((int) leaveRequestRepository.countByUserDepartment(department))
                .approvedLeaveRequests((int) leaveRequestRepository.countByUserDepartmentAndStatus(department, LeaveStatus.APPROVED))
                .pendingLeaveRequests((int) leaveRequestRepository.countByUserDepartmentAndStatus(department, LeaveStatus.PENDING))
                .averageLeaveDuration(calculateAverageLeaveDurationForDepartment(department))
                .build();
    }

    private int countEmployeesOnLeave(String department) {
        LocalDate today = LocalDate.now();
        return leaveRequestRepository.countEmployeesOnLeave(department, today);
    }

    private double calculateAverageLeaveDuration() {
        return leaveRequestRepository.calculateAverageLeaveDuration();
    }

    private double calculateAverageLeaveDurationForDepartment(String department) {
        return leaveRequestRepository.calculateAverageLeaveDurationForDepartment(department);
    }
} 