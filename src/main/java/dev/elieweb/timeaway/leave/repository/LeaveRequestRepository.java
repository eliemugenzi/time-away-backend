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
    @Query(value = "SELECT DISTINCT l FROM LeaveRequest l LEFT JOIN FETCH l.user LEFT JOIN FETCH l.approver WHERE l.user = :user ORDER BY l.createdAt DESC",
           countQuery = "SELECT COUNT(l) FROM LeaveRequest l WHERE l.user = :user")
    Page<LeaveRequest> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    @Query(value = "SELECT DISTINCT l FROM LeaveRequest l LEFT JOIN FETCH l.user LEFT JOIN FETCH l.approver WHERE l.status = :status ORDER BY l.createdAt DESC",
           countQuery = "SELECT COUNT(l) FROM LeaveRequest l WHERE l.status = :status")
    Page<LeaveRequest> findByStatusOrderByCreatedAtDesc(LeaveStatus status, Pageable pageable);

    @Query(value = "SELECT DISTINCT l FROM LeaveRequest l LEFT JOIN FETCH l.user LEFT JOIN FETCH l.approver WHERE l.user = :user AND l.status = :status ORDER BY l.createdAt DESC",
           countQuery = "SELECT COUNT(l) FROM LeaveRequest l WHERE l.user = :user AND l.status = :status")
    Page<LeaveRequest> findByUserAndStatusOrderByCreatedAtDesc(User user, LeaveStatus status, Pageable pageable);

    @Query(value = "SELECT DISTINCT l FROM LeaveRequest l LEFT JOIN FETCH l.user LEFT JOIN FETCH l.approver ORDER BY l.createdAt DESC",
           countQuery = "SELECT COUNT(l) FROM LeaveRequest l")
    Page<LeaveRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);
    
    @Query(value = "SELECT DISTINCT l FROM LeaveRequest l LEFT JOIN FETCH l.user LEFT JOIN FETCH l.approver WHERE " +
           "LOWER(CONCAT(l.user.firstName, ' ', l.user.lastName)) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', :employeeName, '%')) " +
           "ORDER BY l.createdAt DESC",
           countQuery = "SELECT COUNT(l) FROM LeaveRequest l WHERE " +
           "LOWER(CONCAT(l.user.firstName, ' ', l.user.lastName)) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', :employeeName, '%'))")
    Page<LeaveRequest> searchByEmployeeName(String employeeName, Pageable pageable);
    
    @Query(value = "SELECT DISTINCT l FROM LeaveRequest l LEFT JOIN FETCH l.user LEFT JOIN FETCH l.approver WHERE " +
           "(LOWER(CONCAT(l.user.firstName, ' ', l.user.lastName)) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', :employeeName, '%'))) " +
           "AND l.status = :status ORDER BY l.createdAt DESC",
           countQuery = "SELECT COUNT(l) FROM LeaveRequest l WHERE " +
           "(LOWER(CONCAT(l.user.firstName, ' ', l.user.lastName)) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', :employeeName, '%'))) " +
           "AND l.status = :status")
    Page<LeaveRequest> searchByEmployeeNameAndStatus(String employeeName, LeaveStatus status, Pageable pageable);
    
    // Keep non-paginated methods for specific use cases
    @Query("SELECT l FROM LeaveRequest l LEFT JOIN FETCH l.user LEFT JOIN FETCH l.approver WHERE l.user = :user ORDER BY l.createdAt DESC")
    List<LeaveRequest> findByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT l FROM LeaveRequest l LEFT JOIN FETCH l.user LEFT JOIN FETCH l.approver WHERE l.status = :status ORDER BY l.createdAt DESC")
    List<LeaveRequest> findByStatusOrderByCreatedAtDesc(LeaveStatus status);

    long countByStatus(LeaveStatus status);
    
    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.user.department.name = :department")
    long countByUserDepartment(String department);
    
    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.user.department.name = :department AND l.status = :status")
    long countByUserDepartmentAndStatus(String department, LeaveStatus status);

    @Query("SELECT COUNT(DISTINCT l.user) FROM LeaveRequest l WHERE l.user.department.name = :department AND l.startDate <= :date AND l.endDate >= :date AND l.status = 'APPROVED'")
    int countEmployeesOnLeave(String department, LocalDate date);

    @Query("SELECT AVG(TIMESTAMPDIFF(DAY, l.startDate, l.endDate) + 1) FROM LeaveRequest l WHERE l.status = 'APPROVED'")
    Double calculateAverageLeaveDuration();

    @Query("SELECT AVG(TIMESTAMPDIFF(DAY, l.startDate, l.endDate) + 1) FROM LeaveRequest l WHERE l.user.department.name = :department AND l.status = 'APPROVED'")
    Double calculateAverageLeaveDurationForDepartment(String department);

    boolean existsByUserAndStatus(User user, LeaveStatus status);

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM LeaveRequest l " +
           "WHERE l.user = :user AND l.status = 'APPROVED' " +
           "AND ((l.startDate BETWEEN :startDate AND :endDate) " +
           "OR (l.endDate BETWEEN :startDate AND :endDate) " +
           "OR (:startDate BETWEEN l.startDate AND l.endDate) " +
           "OR (:endDate BETWEEN l.startDate AND l.endDate))")
    boolean hasOverlappingApprovedLeaveRequest(User user, LocalDate startDate, LocalDate endDate);

    @Query("SELECT l FROM LeaveRequest l WHERE l.status = :status AND l.startDate BETWEEN :startDate AND :endDate")
    List<LeaveRequest> findByStatusAndStartDateBetween(LeaveStatus status, LocalDate startDate, LocalDate endDate);

    @Query("SELECT l FROM LeaveRequest l WHERE " +
           "(LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', :employeeName, '%'))) AND " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:departmentId IS NULL OR l.department.id = :departmentId)")
    Page<LeaveRequest> searchByEmployeeNameAndStatusAndDepartment(
            String employeeName,
            LeaveStatus status,
            UUID departmentId,
            Pageable pageable);

    @Query("SELECT l FROM LeaveRequest l WHERE " +
           "(LOWER(l.user.firstName) LIKE LOWER(CONCAT('%', :employeeName, '%')) OR " +
           "LOWER(l.user.lastName) LIKE LOWER(CONCAT('%', :employeeName, '%'))) AND " +
           "(:departmentId IS NULL OR l.department.id = :departmentId)")
    Page<LeaveRequest> searchByEmployeeNameAndDepartment(
            String employeeName,
            UUID departmentId,
            Pageable pageable);

    @Query("SELECT l FROM LeaveRequest l WHERE " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:departmentId IS NULL OR l.department.id = :departmentId)")
    Page<LeaveRequest> findByStatusAndDepartment(
            LeaveStatus status,
            UUID departmentId,
            Pageable pageable);
} 