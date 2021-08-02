package com.github.eybv.messenger.api.request;

import com.github.eybv.messenger.application.data.DepartmentData;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class CreateDepartmentRequest {

    @Pattern(regexp = "^(\\p{L}+\\s?)+(\\p{L})$", message = "Invalid department name")
    private String name;

    public DepartmentData toDepartmentData() {
        return DepartmentData.builder().name(name).build();
    }

}
