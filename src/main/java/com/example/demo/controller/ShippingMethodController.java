package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.res.ShippingMethodResponse;
import com.example.demo.service.ShippingMethodService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ShippingMethodController {
    private final ShippingMethodService shippingMethodService;

    @GetMapping("/get-all-shipping")
    public ResponseEntity<List<ShippingMethodResponse>> getShippingMethods() {
        return ResponseEntity.ok(shippingMethodService.getAllShippingMethods());
    }
}
