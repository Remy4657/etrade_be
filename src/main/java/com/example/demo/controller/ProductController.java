package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.res.ProductResponse;
import com.example.demo.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public List<ProductResponse> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductDetail(id));
    }

    @GetMapping("/products/{categoryName}")
    public ResponseEntity<?> getProductsByCategory(
            @PathVariable String categoryName) {

        List<ProductResponse> products = productService.getProductsByCategoryName(categoryName);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/newest")
    public ResponseEntity<?> getNewestProducts() {
        List<ProductResponse> products = productService.getNewestProducts();
        return ResponseEntity.ok(
                products);
    }

    @GetMapping("/products/best-seller")
    public ResponseEntity<?> getBestSellerProducts() {
        List<ProductResponse> products = productService.getBestSellerProducts();
        return ResponseEntity.ok(products);
    }
}
