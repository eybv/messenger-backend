package com.github.eybv.messenger.api.error;

import com.github.eybv.messenger.api.exception.AuthenticationFailureException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Order(1)
@ControllerAdvice
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationFailureException.class)
    public ErrorInfo handleAuthenticationFailureException() {
        return ErrorInfo.builder()
                .message("Access denied")
                .error("Invalid email or password")
                .build();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorInfo errorInfo = ErrorInfo.builder().message("Access denied").build();
        response.getOutputStream().write(objectMapper.writeValueAsBytes(errorInfo));
    }

}
