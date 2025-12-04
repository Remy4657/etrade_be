package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double price;

    // Many Products → One Category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false) // FK
    private CategoryEntity category;
}
