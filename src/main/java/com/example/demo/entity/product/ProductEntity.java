package com.example.demo.entity.product;

import java.math.BigDecimal;
import java.util.List;

import com.example.demo.entity.CategoryEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "product")
public class ProductEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name;
        @Lob
        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(name = "price_original", nullable = false)
        private BigDecimal priceOriginal;

        @Column(name = "discount_percent")
        private Integer discountPercent;

        @Column(name = "thumb")
        private String thumb;

        @Column(name = "thumb_hover")
        private String thumbHover;

        @ManyToOne
        @JoinColumn(name = "category_id", nullable = false)
        @JsonIgnore
        private CategoryEntity category;

        @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
        private List<ProductImageEntity> images;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "product_size", joinColumns = @JoinColumn(name = "product_id"), // FK của User
                        inverseJoinColumns = @JoinColumn(name = "size_id"))
        private List<SizeEntity> sizeList;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "product_color", joinColumns = @JoinColumn(name = "product_id"), // FK của User
                        inverseJoinColumns = @JoinColumn(name = "color_id"))
        private List<ColorEntity> colorList;
}
