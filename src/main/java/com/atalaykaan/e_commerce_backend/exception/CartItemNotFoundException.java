package com.atalaykaan.e_commerce_backend.exception;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(String message) {
        super(message);
    }
}
