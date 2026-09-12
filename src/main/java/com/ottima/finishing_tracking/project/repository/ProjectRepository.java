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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query(
            value = """
                    SELECT
                        p.project_id AS projectId,
                        p.name_ar AS nameAr,
                        p.name_en AS nameEn,
                        p.address_ar AS addressAr,
                        p.address_en AS addressEn,
                        client.username AS clientName,
                        engineer.username AS engineerName,
                        p.overall_status AS overallStatus,
                        p.target_completion_date AS targetCompletionDate,
                        ROUND(COALESCE(SUM((COALESCE(pi.weight_percentage, 0) * COALESCE(pi.completion_percentage, 0)) / 100.0), 0), 2) AS overallProgressPercentage,
                        ROUND(COALESCE(SUM((COALESCE(pi.budget, 0) * COALESCE(pi.completion_percentage, 0)) / 100.0), 0), 2) AS totalCalculatedSpent,
                        p.deletes_at AS deletedAt
                    FROM projects p
                    JOIN users client ON client.user_id = p.client_id
                    JOIN users engineer ON engineer.user_id = p.engineer_id
                    LEFT JOIN project_items pi ON pi.project_id = p.project_id
                    WHERE (:search IS NULL
                        OR LOWER(p.name_ar) LIKE CONCAT('%', LOWER(:search), '%')
                        OR LOWER(p.name_en) LIKE CONCAT('%', LOWER(:search), '%')
                        OR LOWER(COALESCE(p.address_ar, '')) LIKE CONCAT('%', LOWER(:search), '%')
                        OR LOWER(COALESCE(p.address_en, '')) LIKE CONCAT('%', LOWER(:search), '%'))
                      AND (:clientId IS NULL OR p.client_id = :clientId)
                      AND (:engineerId IS NULL OR p.engineer_id = :engineerId)
                      AND ((COALESCE(:isDeleted, FALSE) = TRUE AND p.deletes_at IS NOT NULL)
                        OR (COALESCE(:isDeleted, FALSE) = FALSE AND p.deletes_at IS NULL))
                    GROUP BY
                        p.project_id,
                        p.name_ar,
                        p.name_en,
                        p.address_ar,
                        p.address_en,
                        client.username,
                        engineer.username,
                        p.overall_status,
                        p.target_completion_date,
                        p.deletes_at,
                        p.created_at
                    ORDER BY
                        CASE WHEN p.deletes_at IS NULL THEN p.created_at ELSE p.deletes_at END DESC,
                        p.project_id DESC
                    """,
            countQuery = """
                    SELECT COUNT(*)
                    FROM projects p
                    WHERE (:search IS NULL
                        OR LOWER(p.name_ar) LIKE CONCAT('%', LOWER(:search), '%')
                        OR LOWER(p.name_en) LIKE CONCAT('%', LOWER(:search), '%')
                        OR LOWER(COALESCE(p.address_ar, '')) LIKE CONCAT('%', LOWER(:search), '%')
                        OR LOWER(COALESCE(p.address_en, '')) LIKE CONCAT('%', LOWER(:search), '%'))
                      AND (:clientId IS NULL OR p.client_id = :clientId)
                      AND (:engineerId IS NULL OR p.engineer_id = :engineerId)
                      AND ((COALESCE(:isDeleted, FALSE) = TRUE AND p.deletes_at IS NOT NULL)
                        OR (COALESCE(:isDeleted, FALSE) = FALSE AND p.deletes_at IS NULL))
                    """,
            nativeQuery = true
    )
    Page<AdminProjectSummaryProjection> findAdminProjectSummaries(
            @Param("search") String search,
            @Param("clientId") Long clientId,
            @Param("engineerId") Long engineerId,
            @Param("isDeleted") Boolean isDeleted,
            Pageable pageable
    );

    @Query(value = "SELECT p.projectId FROM Project p WHERE p.deletesAt IS NULL")
    Page<UUID> findAllProjectIds(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN FETCH p.projectItems WHERE p.projectId IN :ids")
    List<Project> findAllWithItemsByIds(@Param("ids") List<UUID> ids);


    boolean existsByProjectIdAndClient_UserId(UUID projectId, Long clientId);

    boolean existsByProjectIdAndEngineer_UserId(UUID projectId, Long engineerId);

    @Query("SELECT p FROM Project p LEFT JOIN FETCH p.projectItems WHERE p.projectId = :projectId")
    Optional<Project> findByIdWithItems(@Param("projectId") UUID projectId);

    long countByOverallStatus(ProjectStatus status);

    long countByOverallStatusAndDeletesAtIsNull(ProjectStatus status);

    long countByDeletesAtIsNull();

    @EntityGraph(attributePaths = {"client", "engineer"})
    Page<Project> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"client", "engineer"})
    Page<Project> findAllByDeletesAtIsNull(Pageable pageable);

    @EntityGraph(attributePaths = {"client", "engineer", "projectItems"})
    Page<Project> findAllByClient_UserId(Long clientId, Pageable pageable);

    @EntityGraph(attributePaths = {"client", "engineer", "projectItems"})
    Page<Project> findAllByClient_UserIdAndDeletesAtIsNull(Long clientId, Pageable pageable);

    @EntityGraph(attributePaths = {"client", "engineer", "projectItems"})
    Page<Project> findAllByEngineer_UserId(Long engineerId, Pageable pageable);

    @EntityGraph(attributePaths = {"client", "engineer", "projectItems"})
    Page<Project> findAllByEngineer_UserIdAndDeletesAtIsNull(Long engineerId, Pageable pageable);
}