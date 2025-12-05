package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;

    private Double priceOriginal;
    private Integer discountPercent;
    private Double salePrice; // computed

    private String thumb; // thumbnail (position=0)
    private String thumbHover; // hover image (position=1)

    private CategoryResponse category;

    private List<ProductImageResponse> images;
    private List<SizeResponse> sizes;
    private List<ColorResponse> colors;
}
