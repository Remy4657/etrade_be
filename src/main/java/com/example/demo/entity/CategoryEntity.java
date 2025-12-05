package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.example.demo.entity.product.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String thumb;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<ProductEntity> products;
}
