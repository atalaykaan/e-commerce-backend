package com.atalaykaan.e_commerce_backend.common.exception;

public class UserWithEmailAlreadyExistsException extends RuntimeException {
    public UserWithEmailAlreadyExistsException(String message) {
        super(message);
    }
}
