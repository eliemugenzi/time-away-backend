package dev.elieweb.timeaway.leave.repository;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.entity.LeaveRequest;
import dev.elieweb.timeaway.leave.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {
    Page<LeaveRequest> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
    Page<LeaveRequest> findByStatusOrderByCreatedAtDesc(LeaveStatus status, Pageable pageable);
    Page<LeaveRequest> findByUserAndStatusOrderByCreatedAtDesc(User user, LeaveStatus status, Pageable pageable);
    Page<LeaveRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT l FROM LeaveRequest l WHERE " +
           "LOWER(CONCAT(l.user.firstName, ' ', l.user.lastName)) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
           "LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
           "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', ?1, '%')) " +
           "ORDER BY l.createdAt DESC")
    Page<LeaveRequest> searchByEmployeeName(String employeeName, Pageable pageable);
    
    @Query("SELECT l FROM LeaveRequest l WHERE " +
           "(LOWER(CONCAT(l.user.firstName, ' ', l.user.lastName)) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
           "LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', ?1, '%')) OR " +
           "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', ?1, '%'))) " +
           "AND l.status = ?2 ORDER BY l.createdAt DESC")
    Page<LeaveRequest> searchByEmployeeNameAndStatus(String employeeName, LeaveStatus status, Pageable pageable);
    
    // Keep non-paginated methods for specific use cases
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

    boolean existsByUserAndStatus(User user, LeaveStatus status);
} 