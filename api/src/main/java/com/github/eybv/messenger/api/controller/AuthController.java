package com.github.eybv.messenger.api.controller;

import com.github.eybv.messenger.api.exception.AuthenticationFailureException;
import com.github.eybv.messenger.api.request.AuthenticationRequest;
import com.github.eybv.messenger.application.data.AccessToken;
import com.github.eybv.messenger.application.service.auth.AuthenticationService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public AccessToken authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return authenticationService
                .authenticate(request.getEmail(), request.getPassword())
                .orElseThrow(AuthenticationFailureException::new);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void check() { }

}
