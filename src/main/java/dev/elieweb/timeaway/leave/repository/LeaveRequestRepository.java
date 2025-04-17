package dev.elieweb.timeaway.leave.repository;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByUser(User user);
    List<LeaveRequest> findByStatus(LeaveStatus status);
    long countByStatus(LeaveStatus status);
    long countByUserDepartment(String department);
    long countByUserDepartmentAndStatus(String department, LeaveStatus status);

    @Query("SELECT COUNT(DISTINCT l.user) FROM LeaveRequest l WHERE l.user.department = ?1 AND l.startDate <= ?2 AND l.endDate >= ?2 AND l.status = 'APPROVED'")
    int countEmployeesOnLeave(String department, LocalDate date);

    @Query("SELECT AVG(TIMESTAMPDIFF(DAY, l.startDate, l.endDate)) FROM LeaveRequest l WHERE l.status = 'APPROVED'")
    Double calculateAverageLeaveDuration();

    @Query("SELECT AVG(TIMESTAMPDIFF(DAY, l.startDate, l.endDate)) FROM LeaveRequest l WHERE l.user.department = ?1 AND l.status = 'APPROVED'")
    Double calculateAverageLeaveDurationForDepartment(String department);
} 