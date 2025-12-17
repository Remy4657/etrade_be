package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.cart.CartEntity;
import com.example.demo.enumtype.CartStatus;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserIdAndStatus(
            Long userId,
            CartStatus status);
}
