package com.github.eybv.messenger.persistence;

import com.github.eybv.messenger.core.user.User;
import com.github.eybv.messenger.core.user.UserRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaUserRepository extends UserRepository, JpaRepository<User, UUID> {

    @Override
    @Query(value = "SELECT * FROM users " +
            "WHERE (:department_id IS NULL OR department_id = :department_id) " +
            "AND (:name IS NULL OR CONCAT(firstname, lastname, patronymic) " +
                    "LIKE CONCAT('%', REGEXP_REPLACE(:name, '\\\\s+', '%'), '%')) " +
            "LIMIT :offset , :limit", nativeQuery = true)
    List<User> findAll(
            @Param("name") String name,
            @Param("department_id") Long departmentId,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

}
