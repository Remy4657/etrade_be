package com.example.demo.entity.cart;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.BaseEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.entity.cart.CartItemEntity;

import com.example.demo.enumtype.CartStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart")
public class CartEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1 user chỉ có 1 cart ACTIVE
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartStatus status = CartStatus.ACTIVE;

    @Column(name = "cart_quantity_total")
    private Integer cartQuantityTotal = 0;

    @Column(name = "cart_total_amount", precision = 12, scale = 2)
    private BigDecimal cartTotalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> cartItemEntity = new ArrayList<>();

}
