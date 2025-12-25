package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.dto.res.ProductDetailResponse;
import com.example.demo.dto.res.ProductResponse;
import com.example.demo.entity.product.ProductEntity;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    // @Override
    // public ProductEntity createProduct(ProductEntity product) {
    // return productRepository.createProduct(product);
    // }

    @Override
    public List<ProductResponse> findAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toProductResponse)
                .toList();

    }

    @Override
    public BigDecimal calculateSalePrice(ProductEntity product) {
        if (product.getDiscountPercent() == null ||
                product.getDiscountPercent() <= 0) {
            return product.getPriceOriginal();
        }

        return product.getPriceOriginal()
                .multiply(BigDecimal.valueOf(100 - product.getDiscountPercent()))
                .divide(BigDecimal.valueOf(100));
    }

    @Override
    public ProductDetailResponse getProductDetail(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return productMapper.toProductDetailResponse(product);
    }
}
