package com.ottima.finishing_tracking.project_chat.repository;

import com.ottima.finishing_tracking.project_chat.entity.ProjectChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProjectChatRepository extends JpaRepository<ProjectChatMessage, UUID> {

    Page<ProjectChatMessage> findByProject_ProjectIdOrderByCreatedAtDesc(UUID projectId, Pageable pageable);

    Page<ProjectChatMessage> findByProject_ProjectIdOrderByCreatedAtAsc(UUID projectId, Pageable pageable);
}
