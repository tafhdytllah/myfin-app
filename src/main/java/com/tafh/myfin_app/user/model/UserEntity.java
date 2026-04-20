package com.tafh.myfin_app.user.model;

import com.tafh.myfin_app.common.exception.DomainException;
import com.tafh.myfin_app.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Entity
@Table(name = "users")
@ToString(exclude = "passwordHash")
@Getter
@NoArgsConstructor
public class UserEntity extends BaseEntity {

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private RoleEnum role;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    public static UserEntity create(
            String username,
            String email,
            String passwordHash,
            RoleEnum role
    ) {
        if (username == null || username.isBlank()) {
            throw new DomainException("Username is required");
        }

        if (email == null || email.isBlank()) {
            throw new DomainException("Email is required");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new DomainException("Password is required");
        }

        if (role == null) {
            throw new DomainException("Role is required");
        }

        UserEntity user = new UserEntity();
        user.username = username;
        user.email = email;
        user.passwordHash = passwordHash;
        user.role = role;
        user.active = true;

        return user;
    }

    public void enable() {
        this.active = true;
    }

    public void disable() {
        this.active = false;
    }

}
