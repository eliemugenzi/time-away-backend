package dev.elieweb.timeaway.leave.repository;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {
    List<LeaveRequest> findByUserOrderByCreatedAtDesc(User user);
    List<LeaveRequest> findByStatusOrderByCreatedAtDesc(LeaveStatus status);
    long countByStatus(LeaveStatus status);
    
    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.user.department.name = ?1")
    long countByUserDepartment(String department);
    
    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.user.department.name = ?1 AND l.status = ?2")
    long countByUserDepartmentAndStatus(String department, LeaveStatus status);

    @Query("SELECT COUNT(DISTINCT l.user) FROM LeaveRequest l WHERE l.user.department.name = ?1 AND l.startDate <= ?2 AND l.endDate >= ?2 AND l.status = 'APPROVED'")
    int countEmployeesOnLeave(String department, LocalDate date);

    @Query("SELECT AVG(TIMESTAMPDIFF(DAY, l.startDate, l.endDate) + 1) FROM LeaveRequest l WHERE l.status = 'APPROVED'")
    Double calculateAverageLeaveDuration();

    @Query("SELECT AVG(TIMESTAMPDIFF(DAY, l.startDate, l.endDate) + 1) FROM LeaveRequest l WHERE l.user.department.name = ?1 AND l.status = 'APPROVED'")
    Double calculateAverageLeaveDurationForDepartment(String department);
} 