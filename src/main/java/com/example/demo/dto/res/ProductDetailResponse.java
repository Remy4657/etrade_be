package com.example.demo.dto.res;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailResponse {
    private Long id;
    private String title;
    private String description;

    private BigDecimal price;
    private Integer discountPercent;
    private BigDecimal salePrice;

    private String pcate;

    private String thumbnail;
    private String hoverThumbnail;

    private List<String> gallery;
    private List<String> sizeAttribute;
    private List<ColorResponse> colorAttribute;
}
