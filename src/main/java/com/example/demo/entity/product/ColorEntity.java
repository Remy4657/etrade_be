package com.example.demo.entity.product;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;
import com.example.demo.entity.product.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "color")
@Data
public class ColorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String colorName;

    private String colorHex; // optional

    @ManyToMany(mappedBy = "colorList")
    @ToString.Exclude
    @JsonIgnore

    private List<ProductEntity> productList;
}
