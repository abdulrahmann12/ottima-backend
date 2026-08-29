package com.ottima.finishing_tracking.logging.service;

import com.ottima.finishing_tracking.logging.dto.response.ActivityLogResponse;
import com.ottima.finishing_tracking.logging.repository.UserActivityLogRepository;
import com.ottima.finishing_tracking.logging.mapper.ActivityLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityLogService {

    private final UserActivityLogRepository repository;
    private final ActivityLogMapper mapper;

    public Page<ActivityLogResponse> getAllLogs(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    public Page<ActivityLogResponse> getLogsByUserId(Long userId, Pageable pageable) {
        return repository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(mapper::toResponse);
    }
}