package com.ottima.finishing_tracking.project.repository;

import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.project.enums.ProjectStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @EntityGraph(attributePaths = {"client", "engineer"})
    Page<Project> findAllByClient_UserId(Long clientId, Pageable pageable);

    @EntityGraph(attributePaths = {"client", "engineer"})
    Page<Project> findAllByEngineer_UserId(Long engineerId, Pageable pageable);

    boolean existsByProjectIdAndClient_UserId(UUID projectId, Long clientId);

    boolean existsByProjectIdAndEngineer_UserId(UUID projectId, Long engineerId);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.projectItems WHERE p.projectId = :projectId")
    Optional<Project> findByIdWithItems(@Param("projectId") UUID projectId);

    long countByOverallStatus(ProjectStatus status);

    @EntityGraph(attributePaths = {"client", "engineer"})
    Page<Project> findAll(Pageable pageable);
}