package com.ottima.finishing_tracking.comment.service;

import com.ottima.finishing_tracking.comment.dto.request.AddCommentRequest;
import com.ottima.finishing_tracking.comment.dto.request.EditCommentRequest;
import com.ottima.finishing_tracking.comment.dto.request.ReplyCommentRequest;
import com.ottima.finishing_tracking.comment.dto.response.CommentResponse;
import com.ottima.finishing_tracking.comment.entity.Comment;
import com.ottima.finishing_tracking.comment.mapper.CommentMapper;
import com.ottima.finishing_tracking.comment.repository.CommentRepository;
import com.ottima.finishing_tracking.daily_update.entity.DailyUpdate;
import com.ottima.finishing_tracking.daily_update.enums.UpdateStatus;
import com.ottima.finishing_tracking.daily_update.repository.DailyUpdateRepository;
import com.ottima.finishing_tracking.exception.*;
import com.ottima.finishing_tracking.security.AuthenticatedUserService;
import com.ottima.finishing_tracking.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Validated
public class CommentService {

    private final CommentRepository commentRepository;
    private final DailyUpdateRepository dailyUpdateRepository;
    private final CommentMapper commentMapper;
    private final AuthenticatedUserService authenticatedUserService;

    @Transactional
    public CommentResponse addComment(UUID dailyUpdateId, @Valid AddCommentRequest request) {
        DailyUpdate dailyUpdate = dailyUpdateRepository.findById(dailyUpdateId)
                .orElseThrow(DailyUpdateNotFoundException::new);

        User currentClient = authenticatedUserService.getCurrentUser();

        if (dailyUpdate.getStatus() != UpdateStatus.APPROVED) {
            throw new UnapprovedDailyUpdateCommentException();
        }

        Long projectOwnerId = dailyUpdate.getProjectItem().getProject().getClient().getUserId();
        if (!projectOwnerId.equals(currentClient.getUserId())) {
            throw new ProjectAccessDeniedException();
        }

        Comment comment = Comment.builder()
                .dailyUpdate(dailyUpdate)
                .client(currentClient)
                .clientComment(request.getClientComment())
                .build();

        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Transactional
    public CommentResponse replyToComment(UUID commentId, ReplyCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        User currentAdmin = authenticatedUserService.getCurrentUser();

        comment.setAdminReply(request.getAdminReply());
        comment.setRepliedByAdmin(currentAdmin);
        comment.setRepliedAt(Instant.now());

        return commentMapper.toResponse(commentRepository.save(comment));
    }

    public Page<CommentResponse> getCommentsForUpdate(UUID dailyUpdateId, Pageable pageable) {
        return commentRepository.findByDailyUpdate_DailyUpdateIdOrderByCreatedAtDesc(dailyUpdateId, pageable)
                .map(commentMapper::toResponse);
    }

    @Transactional
    public CommentResponse editComment(UUID commentId, @Valid EditCommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        User currentClient = authenticatedUserService.getCurrentUser();

        if (!comment.getClient().getUserId().equals(currentClient.getUserId())) {
            throw new CommentAccessDeniedException();
        }

        if (comment.getAdminReply() != null) {
            throw new CommentAlreadyRepliedException();
        }

        comment.setClientComment(request.getClientComment());

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    @Transactional
    public void deleteComment(UUID commentId, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
        if (!isAdmin) {
            User currentClient = authenticatedUserService.getCurrentUser();
            if (!comment.getClient().getUserId().equals(currentClient.getUserId())) {
                throw new CommentAccessDeniedException();
            }
        }

        commentRepository.delete(comment);
    }
}