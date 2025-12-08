package com.example.demo.mapper;

import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.entity.product.ColorEntity;
import com.example.demo.entity.product.ProductEntity;
import com.example.demo.entity.product.ProductImageEntity;
import com.example.demo.entity.product.SizeEntity;

import org.springframework.stereotype.Component;

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
        Double price = p.getPriceOriginal() == null ? 0.0 : p.getPriceOriginal();
        Integer disc = p.getDiscountPercent() == null ? 0 : p.getDiscountPercent();
        double sale = price - (price * disc / 100.0);
        dto.setSalePrice(sale);

        // category
        // dto.setCategory(toCategoryResponse(p.getCategory()));
        dto.setPcate(p.getCategory() != null ? p.getCategory().getName() : null);

        // images
        List<ProductImageResponse> images = p.getImages() == null ? List.of()
                : p.getImages()
                        .stream()
                        .map(this::toImageResponse)
                        .collect(Collectors.toList());

        dto.setImages(images);

        // sizes
        List<SizeResponse> sizes = p.getSizeList() == null ? List.of()
                : p.getSizeList().stream()
                        .map(this::toSizeResponse)
                        .collect(Collectors.toList());
        dto.setSizes(sizes);

        // colors
        List<ColorResponse> colors = p.getColorList() == null ? List.of()
                : p.getColorList().stream()
                        .map(this::toColorResponse)
                        .collect(Collectors.toList());
        dto.setColors(colors);

        return dto;
    }
}
