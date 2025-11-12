package com.atalaykaan.e_commerce_backend.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();


        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();

            errors.put(field, message);
        });

        return new ResponseEntity<>(
                List.of(errors, request.getDescription(false)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {

        return new ResponseEntity<>(
                List.of(ex.getMessage(), request.getDescription(false)),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UserWithEmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserWithEmailAlreadyExistsException(UserWithEmailAlreadyExistsException ex, WebRequest request) {

        return new ResponseEntity<>(
                List.of(ex.getMessage(), request.getDescription(false)),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {

        return new ResponseEntity<>(
                List.of(ex.getMessage(), request.getDescription(false)),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
