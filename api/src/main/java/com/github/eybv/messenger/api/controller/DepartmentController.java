package com.github.eybv.messenger.api.controller;

import com.github.eybv.messenger.api.request.CreateDepartmentRequest;
import com.github.eybv.messenger.api.request.UpdateDepartmentRequest;
import com.github.eybv.messenger.api.security.AdminScope;
import com.github.eybv.messenger.application.data.DepartmentData;
import com.github.eybv.messenger.application.excaption.ResourceNotFoundException;
import com.github.eybv.messenger.application.service.department.DepartmentService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}")
    public DepartmentData getDepartmentById(@PathVariable Long id) {
        return departmentService.findById(id).orElseThrow(() -> {
            String error = "Department with ID `%d` not found";
            return new ResourceNotFoundException(String.format(error, id));
        });
    }

    @GetMapping
    public List<DepartmentData> getDepartmentList(Integer limit, Integer offset) {
        return departmentService.findAll(limit, offset);
    }

    @AdminScope
    @PostMapping
    public DepartmentData createDepartment(@RequestBody @Valid CreateDepartmentRequest request) {
        return departmentService.createDepartment(request.toDepartmentData());
    }

    @AdminScope
    @PatchMapping
    public DepartmentData updateDepartment(@RequestBody @Valid UpdateDepartmentRequest request) {
        return departmentService.updateDepartment(request.toDepartmentData());
    }

    @AdminScope
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }

}
