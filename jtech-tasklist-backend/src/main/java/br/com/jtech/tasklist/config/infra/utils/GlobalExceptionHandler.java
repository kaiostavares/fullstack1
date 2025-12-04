/*
 * @(#)GlobalExceptionHandler.java
 *
 * Copyright (c) J-Tech Solucoes em Informatica.
 * All Rights Reserved.
 *
 * This software is the confidential and proprietary information of J-Tech.
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with J-Tech.
 */
package br.com.jtech.tasklist.config.infra.utils;



import br.com.jtech.tasklist.config.infra.exceptions.*;
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceNotFoundException;
import br.com.jtech.tasklist.application.core.exceptions.DomainResourceAlreadyExists;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Create a global exception handler for intercepting all exceptions in the api.
 *
 * @author angelo.vicente
 * class GlobalExceptionHandler
 **/
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * This method handles spring validations.
     *
     * @param ex Exception thrown.
     * @return Return a {@link ApiError} with an array of errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        ApiError error = new ApiError(HttpStatus.BAD_REQUEST);
        error.setMessage("Error on request");
        error.setTimestamp(LocalDateTime.now());
        error.setSubErrors(subErrors(ex));
        error.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(error);
    }

    private ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(DomainResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(DomainResourceNotFoundException ex) {
        ApiError error = new ApiError(HttpStatus.NOT_FOUND);
        error.setMessage("Resource not found");
        error.setTimestamp(LocalDateTime.now());
        error.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(error);
    }

    @ExceptionHandler(DomainResourceAlreadyExists.class)
    public ResponseEntity<ApiError> handleResourceAlreadyExists(DomainResourceAlreadyExists ex) {
        ApiError error = new ApiError(HttpStatus.CONFLICT);
        error.setMessage("Resource already exists");
        error.setTimestamp(LocalDateTime.now());
        error.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(error);
    }

    private List<ApiSubError> subErrors(MethodArgumentNotValidException ex) {
        List<ApiSubError> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ApiValidationError api = new ApiValidationError(ex.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            errors.add(api);

        }
        return errors;
    }

}