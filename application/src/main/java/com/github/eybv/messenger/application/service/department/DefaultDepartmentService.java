package com.github.eybv.messenger.application.service.department;

import com.github.eybv.messenger.application.data.DepartmentData;
import com.github.eybv.messenger.application.excaption.DataConsistencyViolationException;
import com.github.eybv.messenger.application.excaption.ResourceAlreadyExistsException;
import com.github.eybv.messenger.application.excaption.ResourceNotFoundException;
import com.github.eybv.messenger.core.department.Department;
import com.github.eybv.messenger.core.department.DepartmentRepository;
import com.github.eybv.messenger.core.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultDepartmentService implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DefaultDepartmentService(DepartmentRepository departmentRepository,
                                    UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<DepartmentData> findById(Long id) {
        return departmentRepository.findById(id).map(DepartmentData::fromEntity);
    }

    @Override
    public List<DepartmentData> findAll(Integer limit, Integer offset) {
        limit = Optional.ofNullable(limit).orElse(100);
        offset = Optional.ofNullable(offset).orElse(0);
        return departmentRepository
                .findAll(limit, offset)
                .stream()
                .map(DepartmentData::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentData createDepartment(DepartmentData departmentData) {
        departmentRepository
                .findByName(departmentData.getName())
                .ifPresent(this::throwAlreadyExists);
        Department department = departmentData.toEntity();
        department = departmentRepository.save(department);
        return DepartmentData.fromEntity(department);
    }

    @Override
    public DepartmentData updateDepartment(DepartmentData departmentData) {
        departmentRepository
                .findByName(departmentData.getName())
                .ifPresent(this::throwAlreadyExists);
        departmentRepository
                .findById(departmentData.getId())
                .orElseThrow(() -> throwNotFound(departmentData.getId()));
        departmentRepository.save(departmentData.toEntity());
        return departmentData;
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.findById(id).ifPresentOrElse(department -> {
            if (userRepository.countByDepartment(department) > 0) {
                String error = "Department with ID `%d` is associated with users";
                throw new DataConsistencyViolationException(String.format(error, id));
            }
            departmentRepository.delete(department);
        }, () -> throwNotFound(id));
    }

    // The return value declared for use in Optional.orElseThrow()
    private RuntimeException throwNotFound(Long id) {
        String error = "Department with ID `%d` not found";
        throw new ResourceNotFoundException(String.format(error, id));
    }

    private void throwAlreadyExists(Department department) {
        String error = "Department with name `%s` already exists";
        throw new ResourceAlreadyExistsException(String.format(error, department.getName()));
    }

}
