package com.example.demo.entity.order;

import java.math.BigDecimal;

import com.example.demo.entity.order.*;
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

    @Column(nullable = false)
    private Integer quantity;

    // Giá tại thời điểm add (quan trọng!)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "product_size")
    private String productSize;

    @Column(name = "product_color")
    private String productColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    // FK tới product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;
}
