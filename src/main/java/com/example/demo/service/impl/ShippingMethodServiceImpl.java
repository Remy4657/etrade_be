package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.res.ShippingMethodResponse;
import com.example.demo.repository.ShippingMethodRepository;
import com.example.demo.service.ShippingMethodService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShippingMethodServiceImpl implements ShippingMethodService {
    private final ShippingMethodRepository shippingMethodRepository;

    public List<ShippingMethodResponse> getAllShippingMethods() {
        return shippingMethodRepository.findAll()
                .stream()
                .map(method -> new ShippingMethodResponse(
                        method.getId(),
                        method.getName(),
                        method.getFee(),
                        method.getEstimatedDays(),
                        method.getCode()))
                .toList();
    }
}
