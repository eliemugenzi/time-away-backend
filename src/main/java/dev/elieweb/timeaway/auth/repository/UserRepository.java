package dev.elieweb.timeaway.auth.repository;

import dev.elieweb.timeaway.auth.entity.User;
import dev.elieweb.timeaway.auth.enums.UserRole;
import dev.elieweb.timeaway.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(UserRole role);
    List<User> findByDepartment(Department department);

    @Query("SELECT DISTINCT u.department.name FROM User u")
    List<String> findAllDepartments();

    @Query("SELECT COUNT(u) FROM User u WHERE u.department.name = ?1")
    int countByDepartment(String department);
} 