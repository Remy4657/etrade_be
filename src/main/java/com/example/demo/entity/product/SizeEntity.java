package com.example.demo.entity.product;

import java.util.List;

import com.example.demo.entity.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.demo.entity.product.ProductEntity;

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

@Entity
@Table(name = "size")
@Data
public class SizeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String sizeValue;

    @ManyToMany(mappedBy = "sizeList")
    @ToString.Exclude
    @JsonIgnore

    private List<ProductEntity> productList;
}
