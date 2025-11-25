package com.example.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.demo.entity.ProductEntity;
@Repository
public interface ProductRepository {
    ProductEntity createProduct(ProductEntity product);
    List<ProductEntity> findAllProducts();
    
} 
