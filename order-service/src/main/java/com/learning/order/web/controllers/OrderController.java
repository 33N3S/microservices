package com.learning.order.web.controllers;

import com.learning.order.domain.OrderService;
import com.learning.order.domain.SecurityService;
import com.learning.order.domain.models.CreateOrderRequest;
import com.learning.order.domain.models.CreateOrderResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private SecurityService securityService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request){
        String username = securityService.getLoginUserName();
        log.info("Creating order for user"+username);
        return orderService.createOrder(username,request);
    }

}
