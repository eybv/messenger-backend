package com.github.eybv.messenger.application.service.user;

import com.github.eybv.messenger.application.data.UserData;
import com.github.eybv.messenger.application.excaption.ResourceAlreadyExistsException;
import com.github.eybv.messenger.application.excaption.ResourceNotFoundException;
import com.github.eybv.messenger.application.security.PasswordEncoder;
import com.github.eybv.messenger.core.department.Department;
import com.github.eybv.messenger.core.department.DepartmentRepository;
import com.github.eybv.messenger.core.user.User;
import com.github.eybv.messenger.core.user.UserRepository;
import com.github.eybv.messenger.core.user.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    private final PasswordEncoder passwordEncoder;

    public DefaultUserService(UserRepository userRepository,
                              DepartmentRepository departmentRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserData> findById(String uuid) {
        return userRepository.findById(UUID.fromString(uuid)).map(UserData::fromEntity);
    }

    @Override
    public Optional<UserData> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserData::fromEntity);
    }

    @Override
    public List<UserData> findAll(String name, Long departmentId, Integer limit, Integer offset) {
        limit = Optional.ofNullable(limit).orElse(100);
        offset = Optional.ofNullable(offset).orElse(0);
        return userRepository
                .findAll(name, departmentId, limit, offset)
                .stream()
                .map(UserData::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserData createUser(UserData userData) {
        userRepository
                .findByEmail(userData.getEmail())
                .ifPresent(this::throwAlreadyExists);

        User user = new User();
        user.setEmail(userData.getEmail());
        user.setPassword(passwordEncoder.encode(userData.getPassword()));
        user.setFirstname(userData.getFirstname());
        user.setLastname(userData.getLastname());
        user.setPatronymic(userData.getPatronymic());

        user = userRepository.save(user);

        return UserData.fromEntity(user);
    }

    @Override
    public UserData updateUser(UserData userData) {
        User user = userRepository
                .findById(UUID.fromString(userData.getId()))
                .orElseThrow(() -> throwNotFound(userData.getId()));

        userRepository
                .findByEmail(userData.getEmail())
                .filter(exists -> !exists.equals(user))
                .ifPresent(this::throwAlreadyExists);

        user.setEmail(userData.getEmail());
        user.setFirstname(userData.getFirstname());
        user.setLastname(userData.getLastname());
        user.setPatronymic(userData.getPatronymic());

        userRepository.save(user);

        return UserData.fromEntity(user);
    }

    @Override
    public UserData changeUserDepartment(String uuid, Long departmentId) {
        Department department = departmentRepository
                .findById(departmentId)
                .orElseThrow(() -> throwDepartmentNotFound(departmentId));

        User user = userRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> throwNotFound(uuid));

        user.setDepartment(department);

        userRepository.save(user);

        return UserData.fromEntity(user);
    }

    @Override
    public UserData changeUserRole(String uuid, String role) {
        User user = userRepository
                .findById(UUID.fromString(uuid))
                .orElseThrow(() -> throwNotFound(uuid));

        user.setRole(UserRole.valueOf(role.toUpperCase()));

        userRepository.save(user);

        return UserData.fromEntity(user);
    }

    @Override
    public void changeUserPassword(String uuid, String password) {
        userRepository
                .findById(UUID.fromString(uuid))
                .ifPresentOrElse(user -> {
                    user.setPassword(passwordEncoder.encode(password));
                    userRepository.save(user);
                }, () -> throwNotFound(uuid));
    }

    @Override
    public void disableUser(String uuid) {
        userRepository
                .findById(UUID.fromString(uuid))
                .ifPresentOrElse(user -> {
                    user.setEnabled(false);
                    userRepository.save(user);
                }, () -> throwNotFound(uuid));
    }

    @Override
    public void enableUser(String uuid) {
        userRepository
                .findById(UUID.fromString(uuid))
                .ifPresentOrElse(user -> {
                    user.setEnabled(true);
                    userRepository.save(user);
                }, () -> throwNotFound(uuid));
    }

    // The return value declared for use in Optional.orElseThrow()
    private RuntimeException throwNotFound(String uuid) {
        String error = "User with ID `%s` not found";
        throw new ResourceNotFoundException(String.format(error, uuid));
    }

    private RuntimeException throwDepartmentNotFound(Long id) {
        String error = "Department with ID `%d` not found";
        throw new ResourceNotFoundException(String.format(error, id));
    }

    private void throwAlreadyExists(User user) {
        String error = "User with email `%s` already exists";
        throw new ResourceAlreadyExistsException(String.format(error, user.getEmail()));
    }

}
