package com.thanhhungbui342.cinema.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@MappedSuperclass 
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate 
    @Column(name = "create_at", nullable = false, updatable = false)
    private Instant createAt;

    @LastModifiedDate 
    @Column(name = "updated_at")
    private Instant updatedAt;
}
