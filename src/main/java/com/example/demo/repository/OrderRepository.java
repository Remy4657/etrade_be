package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.order.OrderEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("""
                SELECT o FROM OrderEntity o
                LEFT JOIN FETCH o.orderItemEntity oi
                LEFT JOIN FETCH o.shipping
                LEFT JOIN FETCH o.payment
                WHERE o.id = :id
            """)
    Optional<OrderEntity> findOrderDetailById(@Param("id") Long id);
}
