package com.learning.notification.events;

import com.learning.notification.domain.NotificationService;
import com.learning.notification.domain.OrderEventEntity;
import com.learning.notification.domain.OrderEventRepository;
import com.learning.notification.domain.models.OrderCancelledEvent;
import com.learning.notification.domain.models.OrderCreatedEvent;
import com.learning.notification.domain.models.OrderDeliveredEvent;
import com.learning.notification.domain.models.OrderErrorEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventHandler {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private OrderEventRepository repository;


    @RabbitListener(queues = "${notifications.new-orders-queue}")
    void handleOrderCreatedEvent(OrderCreatedEvent event){
        if(repository.existsByEventId(event.eventId())){
            log.warn("Received duplicate created event with event id : {}",event.eventId());
            return;
        }
        notificationService.sendOrderCreatedNotification(event);
        OrderEventEntity eventEntity = new OrderEventEntity(event.eventId());
        repository.save(eventEntity);
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    void handleOrderDeliveredEvent(OrderDeliveredEvent event){
        if(repository.existsByEventId(event.eventId())){
            log.warn("Received duplicate delivered event with event id : {}",event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        OrderEventEntity eventEntity = new OrderEventEntity(event.eventId());
        repository.save(eventEntity);
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    void handleOrderCancelledEvent(OrderCancelledEvent event){
        if(repository.existsByEventId(event.eventId())){
            log.warn("Received duplicate created event with event id : {}",event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        OrderEventEntity eventEntity = new OrderEventEntity(event.eventId());
        repository.save(eventEntity);
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    void handleOrderErrorEvent(OrderErrorEvent event){
        if(repository.existsByEventId(event.eventId())){
            log.warn("Received duplicate error event with event id : {}",event.eventId());
            return;
        }
        notificationService.sendOrderErrorEventNotification(event);
        OrderEventEntity eventEntity = new OrderEventEntity(event.eventId());
        repository.save(eventEntity);
    }

}
