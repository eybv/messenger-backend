package com.github.eybv.messenger.core.user;

import com.github.eybv.messenger.core.department.Department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    List<User> findAll(String name, Long departmentId, int limit, int offset);

    long countByDepartment(Department department);

    User save(User user);

}
