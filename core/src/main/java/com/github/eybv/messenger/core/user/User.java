package com.github.eybv.messenger.core.user;

import com.github.eybv.messenger.core.department.Department;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue
    @Column(length = 16)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private UserRole role = UserRole.USER;

    @ManyToOne
    @JoinColumn
    private Department department;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    private String patronymic;

    @Column(nullable = false)
    private boolean enabled = true;

    @ElementCollection
    @CollectionTable(uniqueConstraints = {
            @UniqueConstraint(columnNames = {"user_id", "conversation"})
    })
    @Column(name = "conversation", length = 16, nullable = false)
    private Set<UUID> conversations = new HashSet<>();

    public Optional<String> getPatronymic() {
        return Optional.ofNullable(patronymic);
    }

    public Optional<Department> getDepartment() {
        return Optional.ofNullable(department);
    }

}
