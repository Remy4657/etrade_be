package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    @JsonProperty("title")
    private String name;
    private String description;
    @JsonProperty("price")

    private Double priceOriginal;
    private Integer discountPercent;
    private Double salePrice; // computed
    @JsonProperty("thumbnail")
    private String thumb; // thumbnail (position=0)
    @JsonProperty("hoverThumbnail")
    private String thumbHover; // hover image (position=1)

    // private CategoryResponse category;
    private String pcate;
    private List<ProductImageResponse> images;
    private List<SizeResponse> sizes;
    private List<ColorResponse> colors;
}
