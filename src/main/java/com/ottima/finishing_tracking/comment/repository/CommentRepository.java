package com.ottima.finishing_tracking.comment.repository;

import com.ottima.finishing_tracking.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @EntityGraph(attributePaths = {"client", "repliedByAdmin", "dailyUpdate"})
    Page<Comment> findByDailyUpdate_DailyUpdateIdOrderByCreatedAtDesc(UUID dailyUpdateId, Pageable pageable);
}