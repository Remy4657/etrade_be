package com.example.demo.service.impl;

import com.example.demo.mapper.ProductMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.res.ProductResponse;
import com.example.demo.entity.product.ProductEntity;
import com.example.demo.entity.product.WishlistEntity;
import com.example.demo.entity.user.UserEntity;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.WishlistRepository;
import com.example.demo.security.SecurityUtils;
import com.example.demo.service.WishlistService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
        private final ProductMapper productMapper;
        private final WishlistRepository wishlistRepository;
        private final UserRepository userRepository;
        private final ProductRepository productRepository;

        @Override
        public void addToWishlist(Long productId) {
                Long userId = SecurityUtils.getCurrentUserId(); // gọi static method

                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

                ProductEntity product = productRepository.findById(productId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Product not found with id: " + productId));

                boolean exists = wishlistRepository.existsByUserAndProduct(user, product);
                if (exists) {
                        // có thể throw exception nếu muốn báo đã tồn tại
                        return;
                }

                // Tạo mới và lưu
                WishlistEntity wishlist = new WishlistEntity();
                wishlist.setUser(user);
                wishlist.setProduct(product);
                wishlistRepository.save(wishlist);
        }

        @Override
        public List<ProductResponse> getWishlistByUser() {
                Long userId = SecurityUtils.getCurrentUserId(); // gọi static method
                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found"));

                // Lấy danh sách wishlist đã fetch sẵn product
                List<WishlistEntity> wishlistItems = wishlistRepository.findWishlistByUser(user);

                // Map từng Wishlist -> ProductEntity -> ProductResponse
                return wishlistItems.stream()
                                .sorted(Comparator.comparing(WishlistEntity::getAddedDate).reversed())
                                .map(wishlist -> productMapper.toProductResponse(wishlist.getProduct()))
                                .toList();
        }

        @Override
        @Transactional
        public void removeFromWishlist(Long productId) {
                Long userId = SecurityUtils.getCurrentUserId(); // gọi static method

                // kiểm tra user và product tồn tại để tránh lỗi logic
                UserEntity user = userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

                ProductEntity product = productRepository.findById(productId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Product not found with id: " + productId));

                // Xóa nếu tồn tại
                wishlistRepository.deleteByUserAndProduct(user, product);

        }
}
