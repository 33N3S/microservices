package com.learning.order.domain;

import com.learning.order.domain.models.CreateOrderRequest;
import com.learning.order.domain.models.CreateOrderResponse;
import com.learning.order.domain.models.OrderCreatedEvent;
import com.learning.order.domain.models.OrderStatus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("BRAZIL", "MOROCCO", "PORTUGAL");

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderValidator orderValidator;

    @Autowired
    private OrderEventService orderEventService;

    public CreateOrderResponse createOrder(String username, CreateOrderRequest request) {
        orderValidator.validate(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(username);
        OrderEntity savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderNumber -=" + savedOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    public void processNewOrders() {
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);

        log.info("Found {} new orders to process", orders.size());
        for (OrderEntity order : orders) {
            this.process(order);
        }
    }

    private void process(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                log.info("OrderNumber: {} can be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));

            } else {
                log.info("OrderNumber: {} cannot be dilvered", order.getOrderNumber());
                orderEventService.save(
                        OrderEventMapper.buildOrderCancelledEvent(order, "Can't deliver to chosen location"));
            }
        } catch (RuntimeException e) {
            log.error("Failed to process order with orderNumber: {}", order.getOrderNumber());
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, e.getMessage()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(
                order.getDeliveryAddress().country().toUpperCase());
    }
}
