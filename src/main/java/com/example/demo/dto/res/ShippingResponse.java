package com.example.demo.dto.res;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingResponse {
    private Long id;
    private String receiverName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private BigDecimal fee;
    private String shippingMethod;
}
