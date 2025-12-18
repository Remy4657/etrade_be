package com.example.demo.dto.res;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private List<CartItemResponse> items;
    private int cartQuantityTotal;
    private BigDecimal cartTotalAmount;
}
