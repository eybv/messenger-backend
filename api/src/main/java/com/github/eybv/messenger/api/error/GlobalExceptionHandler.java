package com.github.eybv.messenger.api.error;

import com.github.eybv.messenger.application.excaption.DataConsistencyViolationException;
import com.github.eybv.messenger.application.excaption.OperationRejectedException;
import com.github.eybv.messenger.application.excaption.ResourceAlreadyExistsException;
import com.github.eybv.messenger.application.excaption.ResourceNotFoundException;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Order(3)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ErrorInfo handleResourceNotFoundException(ResourceNotFoundException e) {
        return ErrorInfo.builder().message("Not found").error(e.getMessage()).build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ErrorInfo handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        return ErrorInfo.builder().message("Duplicate entry").error(e.getMessage()).build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataConsistencyViolationException.class)
    public ErrorInfo handleDataConsistencyViolationException(DataConsistencyViolationException e) {
        return ErrorInfo.builder().message("Data consistency violation").error(e.getMessage()).build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(OperationRejectedException.class)
    public ErrorInfo handleOperationRejectedException(OperationRejectedException e) {
        return ErrorInfo.builder().message("Operation rejected").error(e.getMessage()).build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorInfo handleMethodArgumentTypeMismatchException() {
        return ErrorInfo.builder().message("Invalid request parameters").build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleException(Exception e) {
        e.printStackTrace();
        return ErrorInfo.builder().message("Something went wrong").build();
    }

}
