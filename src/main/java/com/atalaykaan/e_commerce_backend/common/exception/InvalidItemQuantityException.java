package com.atalaykaan.e_commerce_backend.common.exception;

public class InvalidItemQuantityException extends RuntimeException {
    public InvalidItemQuantityException(String message) {
        super(message);
    }
}
