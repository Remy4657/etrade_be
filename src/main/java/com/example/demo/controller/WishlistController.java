package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.res.ProductResponse;
import com.example.demo.service.WishlistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor

public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/wishlist")
    public ResponseEntity<List<ProductResponse>> getWishlist() {
        return ResponseEntity.ok(wishlistService.getWishlistByUser());
    }

    @PostMapping("/wishlist")
    public ResponseEntity<String> addToWishlist(@RequestParam Long productId) {

        wishlistService.addToWishlist(productId);
        return ResponseEntity.ok("Product added to wishlist successfully!");

    }

    // Xóa sản phẩm khỏi wishlist
    @DeleteMapping("/wishlist")
    public ResponseEntity<String> removeFromWishlist(
            @RequestParam Long productId) {

        wishlistService.removeFromWishlist(productId);
        return ResponseEntity.ok("Product removed from wishlist"); // 204 No Content
    }
}
