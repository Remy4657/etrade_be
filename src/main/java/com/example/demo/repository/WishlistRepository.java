package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.product.ProductEntity;
import com.example.demo.entity.product.WishlistEntity;
import com.example.demo.entity.user.UserEntity;

import java.util.List;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistEntity, Long> {
    // Tìm tất cả sản phẩm yêu thích của một user
    @Query("SELECT w FROM WishlistEntity w JOIN FETCH w.product p WHERE w.user = :user")
    List<WishlistEntity> findWishlistByUser(@Param("user") UserEntity user);

    // Kiểm tra xem user đã thích product chưa
    boolean existsByUserAndProduct(UserEntity user, ProductEntity product);

    // Xóa một mục yêu thích
    int deleteByUserAndProduct(UserEntity user, ProductEntity product);
}