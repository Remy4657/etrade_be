package com.example.demo.service;

import com.example.demo.entity.cart.CartEntity;

public interface CartService {
    void addToCart(Long userId, Long productId, Integer quantity, String size, String color);

}
