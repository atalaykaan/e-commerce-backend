package com.atalaykaan.e_commerce_backend.common.exception;

public class ItemAlreadyInCartException extends RuntimeException {
    public ItemAlreadyInCartException(String message) {
        super(message);
    }
}
