package com.example.demo.entity.order;

import java.math.BigDecimal;

import com.example.demo.entity.product.ProductEntity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "order_item")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    private Integer quantity;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalAmount;
}
