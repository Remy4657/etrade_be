package com.example.demo.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.res.OrderItemResponse;
import com.example.demo.dto.res.OrderResponse;
import com.example.demo.entity.order.OrderEntity;
import com.example.demo.entity.payment.PaymentEntity;
import com.example.demo.entity.shipping.ShippingEntity;

@Component
public class OrderMapper {
    public OrderResponse mapToResponse(OrderEntity order) {
        OrderResponse res = new OrderResponse();
        res.setId(order.getId());
        res.setTotalQuantity(order.getTotalQuantity());
        res.setSubtotalAmount(order.getSubtotalAmount());
        res.setTotalAmount(order.getTotalAmount());
        res.setStatus(order.getStatus());
        res.setCreatedAt(order.getCreatedAt());
        res.setUpdatedAt(order.getUpdatedAt());

        // Shipping
        ShippingEntity shipping = order.getShipping();
        res.setReceiverName(shipping.getReceiverName());
        res.setPhone(shipping.getPhone());
        res.setAddress(shipping.getAddress());
        res.setCity(shipping.getCity());
        res.setNotes(shipping.getNotes());

        // Payment
        PaymentEntity payment = order.getPayment();
        res.setPaymentCode(payment.getCode());
        res.setPaymentName(payment.getName());

        // Items
        List<OrderItemResponse> items = order.getOrderItemEntity()
                .stream()
                .map(item -> {
                    OrderItemResponse i = new OrderItemResponse();
                    i.setProductId(item.getProduct().getId());
                    i.setProductName(item.getProduct().getName());
                    i.setQuantity(item.getQuantity());
                    i.setPrice(item.getPrice());
                    i.setProductSize(item.getProductSize());
                    i.setProductColor(item.getProductColor());
                    return i;
                }).toList();

        res.setItems(items);

        return res;
    }
}
