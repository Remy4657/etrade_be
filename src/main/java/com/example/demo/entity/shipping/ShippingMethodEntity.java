package com.example.demo.entity.shipping;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shipping_method")
public class ShippingMethodEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Ví dụ: STANDARD, EXPRESS
    @Column(nullable = false, unique = true)
    private String code;

    // Ví dụ: Giao hàng tiêu chuẩn
    private String name;

    @Column(precision = 12, scale = 2)
    private BigDecimal fee;

    // Thời gian giao dự kiến (ngày)
    private Integer estimatedDays;
}
