package dev.elieweb.timeaway.leave.repository;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.leave.entity.LeaveBalance;
import dev.elieweb.timeaway.leave.enums.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    List<LeaveBalance> findByUserOrderByCreatedAtDesc(User user);
    List<LeaveBalance> findByUserAndYearOrderByCreatedAtDesc(User user, Integer year);
    Optional<LeaveBalance> findByUserAndTypeAndYear(User user, LeaveType type, Integer year);
    boolean existsByUserAndTypeAndYear(User user, LeaveType type, Integer year);
} 