package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.req.CheckoutRequest;
import com.example.demo.entity.order.OrderEntity;

@Service

public interface OrderService {
    OrderEntity processCheckout(CheckoutRequest request);

    OrderEntity updateTotalAmount(
            Long orderId,
            Long shippingId);
}
