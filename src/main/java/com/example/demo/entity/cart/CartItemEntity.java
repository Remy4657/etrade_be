package com.example.demo.entity.cart;

import java.math.BigDecimal;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.product.ProductEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_item", uniqueConstraints = @UniqueConstraint(columnNames = {
        "cart_id",
        "product_id",
        "product_size",
        "product_color"
}))
public class CartItemEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nhiều item thuộc 1 cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    // FK tới product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private Integer quantity;

    // Giá tại thời điểm add (quan trọng!)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "product_size")
    private String productSize;

    @Column(name = "product_color")
    private String productColor;

}
