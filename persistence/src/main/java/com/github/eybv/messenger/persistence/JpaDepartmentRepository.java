package com.github.eybv.messenger.persistence;

import com.github.eybv.messenger.core.department.Department;
import com.github.eybv.messenger.core.department.DepartmentRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaDepartmentRepository extends DepartmentRepository, JpaRepository<Department, Long> {

    @Override
    default List<Department> findAll(int limit, int offset) {
        return findAll(PageRequest.of(offset / limit, limit)).toList();
    };

}
