package com.ottima.finishing_tracking.logging.service;

import com.ottima.finishing_tracking.logging.entity.UserActivityLog;
import com.ottima.finishing_tracking.logging.repository.UserActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogAsyncService {

    private final UserActivityLogRepository userActivityLogRepository;

    @Async
    public void saveLog(UserActivityLog log) {
        userActivityLogRepository.save(log);
    }
}