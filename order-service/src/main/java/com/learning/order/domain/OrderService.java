package com.learning.order.domain;

import com.learning.order.domain.models.CreateOrderRequest;
import com.learning.order.domain.models.CreateOrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderValidator orderValidator;

    public CreateOrderResponse createOrder(String username, CreateOrderRequest request) {
        orderValidator.validate(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(username);
        OrderEntity savedOrder = this.orderRepository.save(newOrder);
        log.info("Created Order with orderNumber -=" + savedOrder.getOrderNumber());
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }
}
