package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.ProductEntity;
import com.example.demo.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/v1/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/product/add")
    public ProductEntity createProduct(ProductEntity productEntity) {
        return productService.createProduct(productEntity);
    }

    @GetMapping("/products")
    public List<ProductEntity> getAllProducts() {
        return productService.findAllProducts();
    }

}
