package com.github.eybv.messenger.application.service.user;

import com.github.eybv.messenger.application.data.UserData;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<UserData> findById(String uuid);

    Optional<UserData> findByEmail(String email);

    List<UserData> findAll(String name, Long departmentId, Integer limit, Integer offset);

    UserData createUser(UserData userData);

    UserData updateUser(UserData userData);

    UserData changeUserDepartment(String uuid, Long departmentId);

    UserData changeUserRole(String uuid, String role);

    void changeUserPassword(String uuid, String password);

    void disableUser(String uuid);

    void enableUser(String uuid);

}
