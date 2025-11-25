package com.example.demo.entity;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class ProductEntity {
    private Long id;
    private String productName;
    private BigDecimal productPrice;
}
