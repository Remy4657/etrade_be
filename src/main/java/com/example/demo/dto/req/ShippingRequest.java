package com.example.demo.dto.req;

import lombok.Data;

@Data
public class ShippingRequest {
    private String receiverName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String notes;

    private Long shippingMethodId;
}
