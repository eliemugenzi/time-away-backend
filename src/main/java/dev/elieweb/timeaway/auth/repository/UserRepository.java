package dev.elieweb.timeaway.auth.repository;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(UserRole role);

    @Query("SELECT DISTINCT u.department FROM User u")
    List<String> findAllDepartments();

    @Query("SELECT COUNT(u) FROM User u WHERE u.department = ?1")
    int countByDepartment(String department);
} 