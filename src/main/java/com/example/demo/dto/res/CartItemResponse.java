package com.example.demo.dto.res;

import java.math.BigDecimal;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String title;
    private String thumbnail;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Integer cartQuantity;
    private String productSize;
    private String productColor;
}
