package com.example.demo.service;

import com.example.demo.dto.req.CheckoutRequest;
import com.example.demo.entity.order.OrderEntity;

public interface OrderService {
    OrderEntity processCheckout(CheckoutRequest request);
}
