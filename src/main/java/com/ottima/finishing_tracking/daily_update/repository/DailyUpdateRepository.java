package com.ottima.finishing_tracking.daily_update.repository;

import com.ottima.finishing_tracking.daily_update.entity.DailyUpdate;
import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DailyUpdateRepository extends JpaRepository<DailyUpdate, UUID> {

    @EntityGraph(attributePaths = {"images", "engineer", "projectItem", "projectItem.standardItem", "approvedByAdmin"})
    @Query("SELECT d FROM DailyUpdate d WHERE " +
            "(:projectId IS NULL OR d.projectItem.project.projectId = :projectId) AND " +
            "(:projectItemId IS NULL OR d.projectItem.projectItemId = :projectItemId) AND " +
            "(:engineerId IS NULL OR d.engineer.userId = :engineerId) AND " +
            "(:status IS NULL OR d.status = :status) " +
            "ORDER BY d.createdAt DESC")
    Page<DailyUpdate> findFilteredUpdates(
            @Param("projectId") UUID projectId,
            @Param("projectItemId") UUID projectItemId,
            @Param("engineerId") Long engineerId,
            @Param("status") UpdateStatus status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {"images", "engineer", "projectItem", "projectItem.standardItem", "approvedByAdmin"})
    Page<DailyUpdate> findAllByProjectItem_ProjectItemIdAndStatusOrderByCreatedAtDesc(UUID projectItemId, UpdateStatus status, Pageable pageable);
}