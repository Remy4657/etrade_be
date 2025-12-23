package com.example.demo.controller.cart;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.req.AddToCartRequest;
import com.example.demo.dto.req.UpdateCartItemRequest;
import com.example.demo.dto.res.BaseResponse;
import com.example.demo.dto.res.CartResponse;
import com.example.demo.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestBody AddToCartRequest request,
            Authentication authentication) {
        try {
            Long userId = Long.parseLong(authentication.getName());

            cartService.addToCart(
                    userId,
                    request.getProductId(),
                    request.getQuantity(),
                    request.getProductColor(),
                    request.getProductSize());

            return new ResponseEntity<>(
                    new BaseResponse<>("Add to cart successfully", 200),
                    HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse<>(e.getMessage(), 409));
        }

    }

    @GetMapping("/get-current")
    public ResponseEntity<CartResponse> getCurrentCart(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(cartService.getCurrentCart(userId));
    }

    @DeleteMapping("/remove-item/{cartItemId}")
    public ResponseEntity<?> removeCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        try {
            Long userId = Long.parseLong(authentication.getName());

            cartService.removeCartItem(userId, cartItemId);

            return new ResponseEntity<>(
                    new BaseResponse<>("Remove cart item successfully", 200),
                    HttpStatus.OK);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(new BaseResponse<>(e.getMessage(), 409));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateByType(
            @PathVariable Long id,
            @RequestBody UpdateCartItemRequest request) {
        cartService.updateQuantityByType(id, request.getTypeUpdate());
        return ResponseEntity.ok("Update success");
    }

    // @DeleteMapping("/delete-item/{id}")
    // public ResponseEntity<?> delete(@PathVariable Long id) {
    // cartService.deleteCartItem(id);
    // return ResponseEntity.ok("Delete success");
    // }

}
