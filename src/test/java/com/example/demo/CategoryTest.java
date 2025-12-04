package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.example.demo.entity.CategoryEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;

import jakarta.transaction.Transactional;

@SpringBootTest

public class CategoryTest {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    void add() {
        CategoryEntity category = new CategoryEntity();
        UserEntity user = new UserEntity();

        category.setName("Computers");
        category.setName("Laptops");

        categoryRepository.save(category);
    }

}
