package com.learning.notification.events;

import com.learning.notification.domain.models.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class OrderEventHandler {

    @RabbitListener(queues = "${not}")
    void handleOrderCreatedEvent(OrderCreatedEvent event){

    }

}
