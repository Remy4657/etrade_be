package com.example.demo.dto.res;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {

    private int code;
    private Object message;
    private T data;

    public BaseResponse(T data) {
        this.data = data;
        this.message = "Success";
        this.code = 200;
    }

    public BaseResponse(Object message, int code) {
        this.message = message;
        this.code = code;
    }

    public BaseResponse(Object message, T data, int code) {
        this.message = message;
        this.data = data;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public Object getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
