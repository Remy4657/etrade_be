package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Bắt tất cả RuntimeException có gắn @ResponseStatus (409, 404, 401, 400...)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);

        HttpStatus status = (responseStatus != null)
                ? responseStatus.value()
                : HttpStatus.INTERNAL_SERVER_ERROR;

        ApiResponse response = new ApiResponse(status.value(), ex.getMessage());
        return ResponseEntity.status(status).body(response);
    }

    // Lưới an toàn cuối cùng cho lỗi không lường trước (NullPointerException, v.v.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGeneralException(Exception ex) {
        ApiResponse response = new ApiResponse(500,
                (ex.getMessage() != null) ? ex.getMessage() : "Internal server error");
        System.err.println("Unhandled exception: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}