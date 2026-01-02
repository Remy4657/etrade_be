package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.req.CheckoutRequest;
import com.example.demo.dto.res.OrderResponse;
import com.example.demo.entity.order.OrderEntity;

@Service

public interface OrderService {
    OrderEntity processCheckout(CheckoutRequest request);

    public List<OrderResponse> getAllOrders();
}
