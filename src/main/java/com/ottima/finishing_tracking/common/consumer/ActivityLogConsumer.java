package com.ottima.finishing_tracking.common.consumer;

import com.ottima.finishing_tracking.config.rabbitconfig.RabbitConstants;
import com.ottima.finishing_tracking.logging.entity.UserActivityLog;
import com.ottima.finishing_tracking.logging.repository.UserActivityLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ActivityLogConsumer {

    private final UserActivityLogRepository userActivityLogRepository;

    @RabbitListener(queues = RabbitConstants.ACTIVITY_LOG_QUEUE)
    public void consumeActivityLog(UserActivityLog activityLog) {
        try {
            log.debug("Received activity log from RabbitMQ for user: {}", activityLog.getUsername());

            userActivityLogRepository.save(activityLog);

        } catch (Exception e) {
            log.error("Failed to save activity log for user {}. Sending to DLQ. Error: {}",
                    activityLog.getUsername(), e.getMessage(), e);
            throw e;
        }
    }
}