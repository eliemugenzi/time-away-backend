package dev.elieweb.timeaway.department.repository;

import dev.elieweb.timeaway.department.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    Optional<Department> findByName(String name);
    List<Department> findByDeletedFalse();
    List<Department> findByDeletedFalseOrderByCreatedAtDesc();
    boolean existsByName(String name);
    Optional<Department> findByIdAndDeletedFalse(UUID id);
} 