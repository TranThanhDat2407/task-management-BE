package com.example.task_management.module.user.domain;


import com.example.task_management.common.base.time.UuidTimeAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends UuidTimeAuditEntity {

    @Column(name = "email", nullable = false , unique = true, length = 100)
    private String email;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "password", length = 255, nullable = true)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "last_login")
    private Instant lastLogin;
}
