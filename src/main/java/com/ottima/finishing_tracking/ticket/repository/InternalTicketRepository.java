package com.ottima.finishing_tracking.ticket.repository;

import com.ottima.finishing_tracking.ticket.entity.InternalTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InternalTicketRepository extends JpaRepository<InternalTicket, UUID> {

    @EntityGraph(attributePaths = {"project", "sender", "receiver", "attachments", "sender.role", "receiver.role"})
    Optional<InternalTicket> findById(UUID ticketId);

    @EntityGraph(attributePaths = {"project", "sender", "receiver", "sender.role", "receiver.role"})
    Page<InternalTicket> findByProject_ProjectIdOrderByCreatedAtDesc(UUID projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "sender", "receiver", "sender.role", "receiver.role"})
    Page<InternalTicket> findByReceiver_UserIdOrderByCreatedAtDesc(Long receiverId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "sender", "receiver", "sender.role", "receiver.role"})
    Page<InternalTicket> findBySender_UserIdOrderByCreatedAtDesc(Long senderId, Pageable pageable);

    @EntityGraph(attributePaths = {"project", "sender", "receiver", "sender.role", "receiver.role"})
    Page<InternalTicket> findBySender_UserIdOrReceiver_UserIdOrderByCreatedAtDesc(Long senderId, Long receiverId, Pageable pageable);
}