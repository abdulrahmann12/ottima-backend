package com.ottima.finishing_tracking.project_chat.entity;

import com.ottima.finishing_tracking.project.entity.Project;
import com.ottima.finishing_tracking.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "project_chat_messages", indexes = {
        @Index(name = "idx_proj_chat_proj_created", columnList = "project_id, created_at DESC")
})
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID messageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "sender_name")
    private String senderName;

    @Column(name = "sender_role")
    private String senderRole;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "attachments", columnDefinition = "jsonb")
    @Builder.Default
    private List<String> attachments = new ArrayList<>();

    @Column(name = "is_forwarded_from_client", nullable = false)
    @Builder.Default
    private boolean forwardedFromClient = false;

    @Column(name = "daily_update_id")
    private UUID dailyUpdateId;

    @Column(name = "daily_update_title")
    private String dailyUpdateTitle;

    @Column(name = "client_comment_id")
    private UUID clientCommentId;

    @Column(name = "client_name")
    private String clientName;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectChatMessage that)) return false;
        return messageId != null && messageId.equals(that.messageId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
