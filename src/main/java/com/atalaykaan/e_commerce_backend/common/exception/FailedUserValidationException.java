package com.atalaykaan.e_commerce_backend.common.exception;

public class FailedUserValidationException extends RuntimeException{

    public FailedUserValidationException(String message) {
        super(message);
    }
}
