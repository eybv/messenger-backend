package com.github.eybv.messenger.application.data;

import com.github.eybv.messenger.core.department.Department;

import lombok.*;

@Data
@Builder
public class DepartmentData {

    private long id;

    private String name;

    public static DepartmentData fromEntity(Department department) {
        return new DepartmentData(department.getId(), department.getName());
    }

    public Department toEntity() {
        return new Department(id, name);
    }

}
