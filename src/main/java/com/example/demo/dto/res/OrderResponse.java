package com.example.demo.dto.res;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderResponse {
    private Long id;
    private Integer totalQuantity;
    private BigDecimal subtotalAmount;
    private BigDecimal totalAmount;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String receiverName;
    private String phone;
    private String address;
    private String city;
    private String notes;

    private String paymentCode;
    private String paymentName;

    private List<OrderItemResponse> items;
}
