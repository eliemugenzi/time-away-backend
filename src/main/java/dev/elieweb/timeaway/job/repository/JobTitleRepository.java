package dev.elieweb.timeaway.job.repository;

import dev.elieweb.timeaway.job.entity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobTitleRepository extends JpaRepository<JobTitle, UUID> {
    Optional<JobTitle> findByName(String name);
    List<JobTitle> findByDeletedFalseOrderByCreatedAtDesc();
    boolean existsByName(String name);
    boolean existsByNameIgnoreCase(String name);
    List<JobTitle> findByDeletedFalseOrderByNameAsc();
    Optional<JobTitle> findByIdAndDeletedFalse(UUID id);
} 