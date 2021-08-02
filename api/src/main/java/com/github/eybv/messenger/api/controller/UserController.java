package com.github.eybv.messenger.api.controller;

import com.github.eybv.messenger.api.validation.UUID;
import com.github.eybv.messenger.application.data.UserData;
import com.github.eybv.messenger.application.excaption.ResourceNotFoundException;
import com.github.eybv.messenger.application.service.user.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{uuid}")
    public UserData getUserById(@PathVariable @UUID String uuid) {
        return userService.findById(uuid).orElseThrow(() -> {
            String error = "User with ID `%s` not found";
            return new ResourceNotFoundException(String.format(error, uuid));
        });
    }

    @GetMapping
    public List<UserData> getUserList(String email, String name, Long department, Integer limit, Integer offset) {
        if (email != null) {
            return userService.findByEmail(email).map(List::of).orElse(List.of());
        }
        return userService.findAll(name, department, limit, offset);
    }

}
