package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.res.ShippingMethodResponse;

@Service
public interface ShippingMethodService {
    public List<ShippingMethodResponse> getAllShippingMethods();
}
