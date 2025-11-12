package com.atalaykaan.e_commerce_backend.exception;

public class UserWithEmailAlreadyExistsException extends RuntimeException {
    public UserWithEmailAlreadyExistsException(String message) {
        super(message);
    }
}
