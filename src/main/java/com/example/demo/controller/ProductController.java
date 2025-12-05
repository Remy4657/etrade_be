package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.product.ProductEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.service.ProductService;
import com.example.demo.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1")
public class ProductController {
    @Autowired
    private ProductService productService;
    // @Autowired
    // private UserService userService;

    // @PostMapping("/product/add")
    // public ProductEntity createProduct(ProductEntity productEntity) {
    // return productService.createProduct(productEntity);
    // }

    @GetMapping("/products")
    public List<ProductEntity> getAllProducts() {
        return productService.findAllProducts();
    }

    // // USER
    // @GetMapping("/users")
    // public List<UserEntity> getAllUsers() {
    // return userService.getAllUsers();
    // }

    // // USER
    // @GetMapping("/get-username")
    // public List<UserEntity> getUsername(UserEntity userEntity) {
    // return userService.getUserNameEntity(userEntity);
    // }

}
