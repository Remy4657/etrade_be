package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.order.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
