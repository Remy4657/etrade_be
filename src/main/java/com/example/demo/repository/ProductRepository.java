package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.product.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    // ProductEntity createProduct(ProductEntity product);

    // List<ProductEntity> findAllProducts();

    // Query theo category name
    List<ProductEntity> findByCategory_Name(String categoryName);

    List<ProductEntity> findByCreatedAtAfterOrderByCreatedAtDesc(
            LocalDateTime dateTime);

    List<ProductEntity> findByTotalSoldGreaterThanOrderByTotalSoldDesc(
            Long totalSold);
}
