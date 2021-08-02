package com.github.eybv.messenger.core.department;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository {

    Optional<Department> findById(long id);

    Optional<Department> findByName(String name);

    List<Department> findAll(int limit, int offset);

    Department save(Department department);

    void delete(Department department);

}
