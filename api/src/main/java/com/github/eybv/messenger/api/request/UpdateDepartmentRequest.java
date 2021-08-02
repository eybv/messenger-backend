package com.github.eybv.messenger.api.request;

import com.github.eybv.messenger.application.data.DepartmentData;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateDepartmentRequest {

    @NotNull(message = "Department ID should not be empty")
    private Long id;

    @Pattern(regexp = "^(\\p{L}+\\s?)+(\\p{L})$", message = "Invalid department name")
    private String name;

    public DepartmentData toDepartmentData() {
        return DepartmentData.builder().id(id).name(name).build();
    }

}
