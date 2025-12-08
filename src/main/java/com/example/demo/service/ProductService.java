package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.product.ProductEntity;

@Service
public interface ProductService {
    // ProductEntity createProduct(ProductEntity product);
    List<ProductResponse> findAllProducts();
}
