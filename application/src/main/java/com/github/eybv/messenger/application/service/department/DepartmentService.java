package com.github.eybv.messenger.application.service.department;

import com.github.eybv.messenger.application.data.DepartmentData;

import java.util.List;
import java.util.Optional;

public interface DepartmentService {

    Optional<DepartmentData> findById(Long id);

    List<DepartmentData> findAll(Integer limit, Integer offset);

    DepartmentData createDepartment(DepartmentData departmentData);

    DepartmentData updateDepartment(DepartmentData departmentData);

    void deleteDepartment(Long id);

}
