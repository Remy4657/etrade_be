package com.example.demo.dto.req;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long productId;
    private Integer quantity;
    private String productSize;
    private String productColor;
}
