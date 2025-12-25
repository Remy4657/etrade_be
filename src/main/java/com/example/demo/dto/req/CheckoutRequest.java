package com.example.demo.dto.req;

import java.util.List;

import lombok.Data;

@Data
public class CheckoutRequest {

    private Long userId;
    private ShippingRequest shipping;
    private Long paymentId;

    private List<CheckoutItem> items;

    @Data
    public static class CheckoutItem {
        private Long productId;
        private Integer quantity;
        private String productSize;
        private String productColor;
    }
}
