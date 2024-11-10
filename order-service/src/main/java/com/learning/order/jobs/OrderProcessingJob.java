package com.learning.order.jobs;

import com.learning.order.domain.OrderService;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderProcessingJob {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "*/10 * * * * *")
    @SchedulerLock(name = "processNewOrder")
    public void processNewOrder() {
        LockAssert.assertLocked();
        log.info("Processing new orders at {}", Instant.now());
        orderService.processNewOrders();
    }
}
