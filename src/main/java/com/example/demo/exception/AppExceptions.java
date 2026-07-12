
package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class AppExceptions {

    @ResponseStatus(HttpStatus.CONFLICT)
    public static class ConflictRequestError extends RuntimeException {

        public ConflictRequestError(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NotFoundError extends RuntimeException {
        public NotFoundError(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequestError extends RuntimeException {
        public BadRequestError(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class UnauthorizedError extends RuntimeException {
        public UnauthorizedError(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public static class ForbiddenError extends RuntimeException {
        public ForbiddenError(String message) {
            super(message);
        }
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public static class InternalServerError extends RuntimeException {
        public InternalServerError(String message) {
            super(message);
        }
    }
}
