package com.example.demo.dto.res;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetailResponse {
    private Long id;
    private Integer totalQuantity;
    private BigDecimal subtotalAmount;
    private BigDecimal totalAmount;
    private String status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private ShippingResponse shipping;
    private PaymentResponse payment;
    private List<OrderItemResponse> items;
}
