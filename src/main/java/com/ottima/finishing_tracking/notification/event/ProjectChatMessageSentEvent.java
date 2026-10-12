package com.ottima.finishing_tracking.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class ProjectChatMessageSentEvent {
    private UUID projectId;
    private Long senderId;
    private String senderName;
    private String senderRole;
    private String projectNameEn;
    private String projectNameAr;
    private String messageSnippet;
    private List<Long> recipientIds;
}
