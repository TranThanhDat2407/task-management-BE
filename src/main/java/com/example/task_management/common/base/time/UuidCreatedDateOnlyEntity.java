package com.example.task_management.common.base.time;

import com.example.task_management.common.base.id.UuidV7Entity;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class UuidCreatedDateOnlyEntity extends UuidV7Entity {
    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;
}
