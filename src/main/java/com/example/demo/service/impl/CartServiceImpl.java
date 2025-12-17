package com.example.demo.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Transactional
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired

    private CartItemRepository cartItemRepository;
    @Autowired

    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    public void addToCart(
            Long userId,
            Long productId,
            Integer quantity,
            String size,
            String color) {
        CartEntity cart = cartRepository
                .findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseGet(() -> createCart(userId));

        CartItemEntity item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), productId)
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            ProductEntity product = productRepository
                    .findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            CartItemEntity newItem = new CartItemEntity();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setPrice(productService.calculateSalePrice(product));
            newItem.setProductSize(size);
            newItem.setProductColor(color);
            cart.getItems().add(newItem);
            recalculateCart(cart);
            cartItemRepository.save(newItem);
        }
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

        for (CartItemEntity item : cart.getItems()) {
            totalQuantity += item.getQuantity();

            BigDecimal itemTotal = item.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            totalAmount = totalAmount.add(itemTotal);
        }

        cart.setCartQuantityTotal(totalQuantity);
        cart.setCartTotalAmount(totalAmount);
    }
}