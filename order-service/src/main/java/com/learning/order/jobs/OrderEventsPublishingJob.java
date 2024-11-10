package com.learning.order.jobs;

import com.learning.order.domain.OrderEventService;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class OrderEventsPublishingJob {
    @Autowired
    private OrderEventService orderEventService;

    // this allows to run every 5 seconds
    @Scheduled(cron = "*/5 * * * * *")
    @SchedulerLock(name = "publishOrderEvents")
    public void publishOrderEvents() {
        LockAssert.assertLocked();
        log.info("Publishing Order events at {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
