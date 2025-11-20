package com.atalaykaan.e_commerce_backend.common.exception;

public class InvalidProductQuantityException extends RuntimeException {
    public InvalidProductQuantityException(String message) {
        super(message);
    }
}
