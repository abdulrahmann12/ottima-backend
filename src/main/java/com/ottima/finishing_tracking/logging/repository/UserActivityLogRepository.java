package com.ottima.finishing_tracking.logging.repository;

import com.ottima.finishing_tracking.logging.entity.UserActivityLog;
import com.ottima.finishing_tracking.logging.enums.ActionType;
import com.ottima.finishing_tracking.logging.enums.ActivityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserActivityLogRepository extends JpaRepository<UserActivityLog, UUID> {

    Page<UserActivityLog> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    @Query("""
        SELECT l FROM UserActivityLog l
        WHERE (:userId IS NULL OR l.userId = :userId)
          AND (:action IS NULL OR l.action = :action)
          AND (:status IS NULL OR l.status = :status)
          AND (:entityName IS NULL OR :entityName = '' OR LOWER(l.entityName) LIKE LOWER(CONCAT('%', :entityName, '%')))
        ORDER BY l.createdAt DESC
    """)
    Page<UserActivityLog> findWithFilters(
            @Param("userId") Long userId,
            @Param("action") ActionType action,
            @Param("status") ActivityStatus status,
            @Param("entityName") String entityName,
            Pageable pageable
    );
}