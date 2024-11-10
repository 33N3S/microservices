package com.learning.order.domain;

import com.learning.order.ApplicationProperties;
import com.learning.order.domain.models.OrderCancelledEvent;
import com.learning.order.domain.models.OrderCreatedEvent;
import com.learning.order.domain.models.OrderDeliveredEvent;
import com.learning.order.domain.models.OrderErrorEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ApplicationProperties properties;

    public void publish(OrderCreatedEvent event) {
        this.send(properties.newOrdersQueue(), event);
    }

    public void publish(OrderDeliveredEvent event) {
        this.send(properties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderCancelledEvent event) {
        this.send(properties.cancelledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event) {
        this.send(properties.errorOrdersQueue(), event);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }
}
