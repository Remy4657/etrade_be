package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.res.CartItemResponse;
import com.example.demo.dto.res.CartResponse;
import com.example.demo.entity.cart.CartEntity;
import com.example.demo.entity.cart.CartItemEntity;
import com.example.demo.entity.product.ProductEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.enumtype.CartStatus;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired

    private CartItemRepository cartItemRepository;
    @Autowired

    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Transactional
    public void addToCart(
            Long userId,
            Long productId,
            Integer quantity,
            String size,
            String color) {
        // tìm xem user có giỏ hàng chưa (bảng cart)
        CartEntity cart = cartRepository
                .findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> createCart(userId));
        ProductEntity product = productRepository
                .findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CartItemEntity item = cartItemRepository
                .findByCartAndProductAndProductSizeAndProductColor(
                        cart, product, size, color)
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItemEntity newItem = new CartItemEntity();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(productService.calculateSalePrice(product));
            newItem.setProductSize(size);
            newItem.setProductColor(color);

            cart.getCartItemEntity().add(newItem); // sync 2 chiều
        }
        recalculateCart(cart);

    }

    private CartEntity createCart(Long userId) {
        CartEntity cart = new CartEntity();
        UserEntity user = new UserEntity();
        user.setId(userId);
        cart.setUser(user);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setCartQuantityTotal(0);
        cart.setCartTotalAmount(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    private void recalculateCart(CartEntity cart) {
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItemEntity item : cart.getCartItemEntity()) {
            totalQuantity += item.getQuantity();

            BigDecimal itemTotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);
        }

        cart.setCartQuantityTotal(totalQuantity);
        cart.setCartTotalAmount(totalAmount);
    }

    @Override
    public CartResponse getCurrentCart(Long userId) {
        CartEntity cart = cartRepository
                .findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElse(null);

        // Nếu chưa có cart → trả cart rỗng
        if (cart == null) {
            return new CartResponse(
                    List.of(),
                    0,
                    BigDecimal.ZERO);
        }

        // Map entity → response
        List<CartItemResponse> items = cart.getCartItemEntity()
                .stream()
                .map(this::mapToItemResponse)
                .toList();
        return new CartResponse(
                items,
                cart.getCartQuantityTotal(),
                cart.getCartTotalAmount());
    }

    private CartItemResponse mapToItemResponse(CartItemEntity item) {
        CartItemResponse res = new CartItemResponse();
        res.setId(item.getId());
        res.setProductId(item.getProduct().getId());
        res.setTitle(item.getProduct().getName());
        res.setThumbnail(item.getProduct().getThumb());
        res.setPrice(item.getProduct().getPriceOriginal());
        res.setSalePrice(item.getPrice());
        res.setCartQuantity(item.getQuantity());
        res.setProductSize(item.getProductSize());
        res.setProductColor(item.getProductColor());
        return res;
    }
}