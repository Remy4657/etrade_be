package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CategoryEntity;

@Service
public interface CategoryService {
    List<CategoryEntity> getAllCategories();
}
