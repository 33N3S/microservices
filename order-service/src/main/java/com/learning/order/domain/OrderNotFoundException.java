package com.learning.order.domain;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException forOrderNumber(String orderNum) {
        return new OrderNotFoundException("Order with Number " + orderNum + " not found");
    }
}
