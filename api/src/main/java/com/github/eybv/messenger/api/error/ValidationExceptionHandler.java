package com.github.eybv.messenger.api.error;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@Order(2)
@ControllerAdvice
public class ValidationExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorInfo.ErrorInfoBuilder errorInfoBuilder = ErrorInfo.builder();
        errorInfoBuilder.message("Invalid request parameters");
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errorInfoBuilder.error(fieldError.getDefaultMessage());
        });
        return errorInfoBuilder.build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorInfo handleConstraintViolationException(ConstraintViolationException e) {
        ErrorInfo.ErrorInfoBuilder errorInfoBuilder = ErrorInfo.builder();
        errorInfoBuilder.message("Invalid request parameters");
        e.getConstraintViolations().forEach(constraintViolation -> {
            errorInfoBuilder.error(constraintViolation.getMessage());
        });
        return errorInfoBuilder.build();
    }

}
