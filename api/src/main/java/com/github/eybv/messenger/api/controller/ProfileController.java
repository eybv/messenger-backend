package com.github.eybv.messenger.api.controller;

import com.github.eybv.messenger.api.request.ChangePasswordRequest;
import com.github.eybv.messenger.application.data.UserData;
import com.github.eybv.messenger.application.service.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserData whoami(@AuthenticationPrincipal UserData self) {
        return self;
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@AuthenticationPrincipal UserData self,
                               @RequestBody @Valid ChangePasswordRequest request) {
        userService.changeUserPassword(self.getId(), request.getPassword());
    }

}
