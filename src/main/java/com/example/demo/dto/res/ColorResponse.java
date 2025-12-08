package com.example.demo.dto.res;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColorResponse {
    private Long id;
    @JsonProperty("color")
    private String colorName;
    private String colorHex;
}
