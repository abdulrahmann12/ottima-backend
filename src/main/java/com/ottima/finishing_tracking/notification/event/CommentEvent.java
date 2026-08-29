package com.ottima.finishing_tracking.notification.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CommentEvent {
    private UUID commentId;
    private Long receiverId;
    private String senderName;
    private String projectNameAr;
    private String projectNameEn;
    private boolean isReply;
}