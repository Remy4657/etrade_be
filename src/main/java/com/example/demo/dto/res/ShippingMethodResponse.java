package com.example.demo.dto.res;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShippingMethodResponse {

    private String name;
    private BigDecimal fee;
    private Integer estimatedDays;
}
