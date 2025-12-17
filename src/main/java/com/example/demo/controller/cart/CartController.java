package com.example.demo.controller.cart;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.req.AddToCartRequest;
import com.example.demo.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request,
            Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());

        cartService.addToCart(
                userId,
                request.getProductId(),
                request.getQuantity(),
                request.getProductColor(),
                request.getProductSize());

        return ResponseEntity.ok().build();
    }
}
