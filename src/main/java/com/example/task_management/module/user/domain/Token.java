package com.example.task_management.module.user.domain;

import com.example.task_management.common.base.time.UuidCreatedDateOnlyEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Token extends UuidCreatedDateOnlyEntity {
    @Column(name = "token_value", unique = true, nullable = false, columnDefinition = "TEXT")
    private String tokenValue;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiredDate;

    @Column(name = "is_revoked")
    private Boolean isRevoked;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
