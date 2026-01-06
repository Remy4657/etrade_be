package com.example.demo.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.dto.res.OrderDetailResponse;
import com.example.demo.dto.res.OrderItemResponse;
import com.example.demo.dto.res.PaymentResponse;
import com.example.demo.dto.res.ShippingResponse;
import com.example.demo.entity.order.OrderEntity;
import com.example.demo.entity.payment.PaymentEntity;
import com.example.demo.entity.shipping.ShippingEntity;

@Component
public class OrderDetailMapper {
    public OrderDetailResponse mapToResponse(OrderEntity order) {
        OrderDetailResponse res = new OrderDetailResponse();
        res.setId(order.getId());
        res.setTotalQuantity(order.getTotalQuantity());
        res.setSubtotalAmount(order.getSubtotalAmount());
        res.setTotalAmount(order.getTotalAmount());
        res.setStatus(order.getStatus());
        res.setNotes(order.getNotes());
        res.setCreatedAt(order.getCreatedAt());
        res.setUpdatedAt(order.getUpdatedAt());

        // shipping
        ShippingEntity s = order.getShipping();
        ShippingResponse sr = new ShippingResponse();
        sr.setId(s.getId());
        sr.setReceiverName(s.getReceiverName());
        sr.setPhone(s.getPhone());
        sr.setEmail(s.getEmail());
        sr.setAddress(s.getAddress());
        sr.setCity(s.getCity());
        sr.setShippingMethod(s.getShippingMethod().getName());
        res.setShipping(sr);

        // payment
        PaymentEntity p = order.getPayment();
        PaymentResponse pr = new PaymentResponse();
        pr.setId(p.getId());
        pr.setCode(p.getCode());
        pr.setName(p.getName());
        res.setPayment(pr);

        // items
        List<OrderItemResponse> items = order.getOrderItemEntity()
                .stream()
                .map(oi -> {
                    OrderItemResponse ir = new OrderItemResponse();
                    ir.setProductId(oi.getProduct().getId());
                    ir.setProductName(oi.getProduct().getName());
                    ir.setQuantity(oi.getQuantity());
                    ir.setPrice(oi.getPrice());
                    ir.setProductSize(oi.getProductSize());
                    ir.setProductColor(oi.getProductColor());
                    return ir;
                })
                .toList();

        res.setItems(items);

        return res;
    }
}
