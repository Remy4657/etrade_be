package com.example.demo.dto.res;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
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

    private BigDecimal priceOriginal;
    private Integer discountPercent;
    private BigDecimal salePrice; // computed
    @JsonProperty("thumbnail")
    private String thumb; // thumbnail (position=0)
    @JsonProperty("hoverThumbnail")
    private String thumbHover; // hover image (position=1)

    // private CategoryResponse category;
    private String pcate;
    // private List<ProductImageResponse> gallery;
    private List<String> gallery;
    private List<String> sizeAttribute;
    private List<ColorResponse> colorAttribute;
}
