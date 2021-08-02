package com.github.eybv.messenger.api.controller;

import com.github.eybv.messenger.api.request.ChangePasswordRequest;
import com.github.eybv.messenger.api.request.CreateAccountRequest;
import com.github.eybv.messenger.api.request.UpdateAccountRequest;
import com.github.eybv.messenger.api.security.AdminScope;
import com.github.eybv.messenger.api.validation.UserRole;
import com.github.eybv.messenger.api.validation.UUID;
import com.github.eybv.messenger.application.data.UserData;
import com.github.eybv.messenger.application.service.user.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@AdminScope
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserData createAccount(@RequestBody @Valid CreateAccountRequest request) {
        return userService.createUser(request.toUserData());
    }

    @PutMapping
    public UserData updateAccount(@RequestBody @Valid UpdateAccountRequest request) {
        return userService.updateUser(request.toUserData());
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable @UUID String id,
                               @RequestBody @Valid ChangePasswordRequest request) {
        userService.changeUserPassword(id, request.getPassword());
    }

    @PutMapping("/{id}/department/{departmentId}")
    public UserData changeDepartment(@PathVariable @UUID String id,
                                     @PathVariable Long departmentId) {
        return userService.changeUserDepartment(id, departmentId);
    }

    @PutMapping("/{id}/role/{role}")
    public UserData changeRole(@PathVariable @UUID String id,
                               @PathVariable @UserRole String role) {
        return userService.changeUserRole(id, role);
    }

    @PutMapping("/{id}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disableAccount(@PathVariable @UUID String id) {
        userService.disableUser(id);
    }

    @PutMapping("/{id}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enableAccount(@PathVariable @UUID String id) {
        userService.enableUser(id);
    }

}
