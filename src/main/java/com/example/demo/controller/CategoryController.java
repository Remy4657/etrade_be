package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.CategoryEntity;
import com.example.demo.service.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    // @Autowired
    // private UserService userService;

    @GetMapping("/categories")
    public List<CategoryEntity> getAllProducts() {
        return categoryService.getAllCategories();
    }
    // @PostMapping("/product/add")
    // public ProductEntity createProduct(ProductEntity productEntity) {
    // return productService.createProduct(productEntity);
    // }

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
