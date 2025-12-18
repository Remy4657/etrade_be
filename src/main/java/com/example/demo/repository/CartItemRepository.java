package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.cart.CartEntity;
import com.example.demo.entity.cart.CartItemEntity;
import com.example.demo.entity.product.ProductEntity;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    Optional<CartItemEntity> findByCartIdAndProductId(
            Long cartId,
            Long productId);

    Optional<CartItemEntity> findByCartAndProductAndProductSizeAndProductColor(
            CartEntity cart,
            ProductEntity product,
            String productSize,
            String productColor);
}
