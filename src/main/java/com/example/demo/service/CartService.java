package com.example.demo.service;

import com.example.demo.dto.res.CartResponse;

public interface CartService {
    void addToCart(Long userId, Long productId, Integer quantity, String size, String color);

    CartResponse getCurrentCart(Long userId);
}
