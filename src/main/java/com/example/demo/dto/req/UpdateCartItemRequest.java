package com.example.demo.dto.req;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private String typeUpdate; // increase | decrease
}
