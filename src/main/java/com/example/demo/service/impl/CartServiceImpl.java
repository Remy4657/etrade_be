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
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Transactional
@Service
@RequiredArgsConstructor // dùng khời tạo constructor cho thuộc tính final (@Autowired không áp dụng dc
                         // trong case này)

public class CartServiceImpl implements CartService {

    private final CartItemMapper cartItemMapper;
    @Autowired
    private CartRepository cartRepository;
    @Autowired

    private CartItemRepository cartItemRepository;
    @Autowired

    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Override
    public void addToCart(
            Long userId,
            Long productId,
            Integer quantity,
            String color,
            String size) {

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
            // nếu sp được add vào cart trước đó rồi
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
        cartRepository.save(cart);
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
                .map(cartItemMapper::toCartItemResponse)
                .toList();
        return new CartResponse(
                items,
                cart.getCartQuantityTotal(),
                cart.getCartTotalAmount());
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {

        CartEntity cart = cartRepository
                .findByUserIdAndStatus(userId, CartStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItemEntity item = cartItemRepository
                .findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        // bảo mật: item phải thuộc cart của user
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        // remove 2 chiều
        cart.getCartItemEntity().remove(item);
        item.setCart(null);

        recalculateCart(cart);
    }

    // start: increase or decrease item in cart
    public void updateQuantityByType(Long cartItemId, String typeUpdate) {

        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        CartEntity cart = item.getCart();

        if ("increase".equalsIgnoreCase(typeUpdate)) {
            item.setQuantity(item.getQuantity() + 1);
            // item.setTotalAmount(
            // item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else if ("decrease".equalsIgnoreCase(typeUpdate)) {
            int newQty = item.getQuantity() - 1;
            if (newQty <= 0) {
                cartItemRepository.delete(item);
            } else {
                item.setQuantity(newQty);
                // item.setTotalAmount(
                // item.getPrice().multiply(BigDecimal.valueOf(newQty)));
            }
        } else {
            throw new IllegalArgumentException("Invalid type: " + typeUpdate);
        }

        recalculateCart(cart);
    }
    // end: increase or decrease item in cart

    // start: delete item in cart
    // public void deleteCartItem(Long cartItemId) {

    // CartItemEntity item = cartItemRepository.findById(cartItemId)
    // .orElseThrow(() -> new RuntimeException("Cart item not found"));

    // CartEntity cart = item.getCart();

    // cartItemRepository.delete(item);

    // recalculateCart(cart);
    // }
    // end: delete item in cart
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

}