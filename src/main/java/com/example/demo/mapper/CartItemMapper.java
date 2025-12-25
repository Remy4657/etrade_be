package com.example.demo.mapper;

import org.springframework.stereotype.Component;

import com.example.demo.dto.res.CartItemResponse;
import com.example.demo.entity.cart.CartItemEntity;

@Component
public class CartItemMapper {
    public CartItemResponse toCartItemResponse(CartItemEntity item) {
        if (item == null)
            return null;
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
