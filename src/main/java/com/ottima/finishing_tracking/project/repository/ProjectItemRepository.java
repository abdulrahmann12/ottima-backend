package com.ottima.finishing_tracking.project.repository;

import com.ottima.finishing_tracking.project.entity.ProjectItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectItemRepository extends JpaRepository<ProjectItem, UUID> {

    List<ProjectItem> findAllByProject_ProjectIdOrderBySequenceOrderAsc(UUID projectId);

    boolean existsByProject_ProjectIdAndStandardItem_ItemId(UUID projectId, UUID standardItemId);

    boolean existsByStandardItem_ItemId(UUID standardItemId);

    @Query("SELECT COALESCE(SUM(pi.weightPercentage), 0) FROM ProjectItem pi WHERE pi.project.projectId = :projectId")
    BigDecimal sumWeightPercentageByProjectId(@Param("projectId") UUID projectId);

    Optional<ProjectItem> findByProjectItemIdAndProject_ProjectId(UUID projectItemId, UUID projectId);

    @Query("SELECT COALESCE(MAX(pi.sequenceOrder), 0) FROM ProjectItem pi WHERE pi.project.projectId = :projectId")
    Integer findMaxSequenceOrderByProjectId(@Param("projectId") UUID projectId);
}