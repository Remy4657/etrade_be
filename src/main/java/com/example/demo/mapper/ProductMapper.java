package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.dto.res.CategoryResponse;
import com.example.demo.dto.res.ColorResponse;
import com.example.demo.dto.res.ProductImageResponse;
import com.example.demo.dto.res.ProductResponse;
import com.example.demo.dto.res.SizeResponse;
import com.example.demo.entity.*;
import com.example.demo.entity.product.ColorEntity;
import com.example.demo.entity.product.ProductEntity;
import com.example.demo.entity.product.ProductImageEntity;
import com.example.demo.entity.product.SizeEntity;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public CategoryResponse toCategoryResponse(CategoryEntity c) {
        if (c == null)
            return null;
        CategoryResponse dto = new CategoryResponse();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setThumb(c.getThumb());
        return dto;
    }

    public ProductImageResponse toImageResponse(ProductImageEntity img) {
        if (img == null)
            return null;
        ProductImageResponse dto = new ProductImageResponse();
        dto.setId(img.getId());
        dto.setImageUrl(img.getImageUrl());
        return dto;
    }

    public SizeResponse toSizeResponse(SizeEntity s) {
        if (s == null)
            return null;
        SizeResponse dto = new SizeResponse();
        dto.setId(s.getId());
        dto.setSizeValue(s.getSizeValue());
        return dto;
    }

    public ColorResponse toColorResponse(ColorEntity c) {
        if (c == null)
            return null;
        ColorResponse dto = new ColorResponse();
        dto.setId(c.getId());
        dto.setColorName(c.getColorName());
        dto.setColorHex(c.getColorHex());
        return dto;
    }

    public ProductResponse toProductResponse(ProductEntity p) {
        if (p == null)
            return null;

        ProductResponse dto = new ProductResponse();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setThumb(p.getThumb());
        dto.setThumbHover(p.getThumbHover());
        dto.setDescription(p.getDescription());
        dto.setPriceOriginal(p.getPriceOriginal());
        dto.setDiscountPercent(p.getDiscountPercent());

        // compute salePrice
        BigDecimal price = p.getPriceOriginal() != null
                ? p.getPriceOriginal()
                : BigDecimal.ZERO;

        Integer disc = p.getDiscountPercent() != null
                ? p.getDiscountPercent()
                : 0;

        BigDecimal discountRate = BigDecimal.valueOf(disc)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal salePrice = price.subtract(
                price.multiply(discountRate));
        dto.setSalePrice(salePrice);

        // category
        // dto.setCategory(toCategoryResponse(p.getCategory()));
        dto.setPcate(p.getCategory() != null ? p.getCategory().getName() : null);

        // images
        // List<ProductImageResponse> images = p.getImages() == null ? List.of()
        // : p.getImages()
        // .stream()
        // .map(this::toImageResponse)
        // .collect(Collectors.toList());
        // result:
        // "gallery": [
        // {
        // "id": 1,
        // "imageUrl": "/images/product/product-big-01.png"
        // },
        // {
        // "id": 2,
        // "imageUrl": "/images/product/product-big-02.png"
        // }
        // ],
        List<String> gallery = p.getImages() == null ? List.of()
                : p.getImages().stream()
                        .map(img -> img.getImageUrl())
                        .toList();

        dto.setGallery(gallery);

        // sizes
        List<String> sizes = p.getSizeList() == null ? List.of()
                : p.getSizeList().stream()
                        .map(size -> size.getSizeValue()).toList();
        dto.setSizeAttribute(sizes);

        // colors
        List<ColorResponse> colors = p.getColorList() == null ? List.of()
                : p.getColorList().stream()
                        .map(this::toColorResponse)
                        .collect(Collectors.toList());
        dto.setColorAttribute(colors);

        return dto;
    }
}
