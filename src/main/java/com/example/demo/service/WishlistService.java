package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.res.ProductDetailResponse;
import com.example.demo.dto.res.ProductResponse;
import com.example.demo.entity.product.ProductEntity;

@Service
public interface WishlistService {
    void addToWishlist(Long productId);

    List<ProductResponse> getWishlistByUser();

    void removeFromWishlist(Long productId);
}
