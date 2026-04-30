package com.tafh.myfin_app.category.model;

import com.tafh.myfin_app.common.exception.DomainException;
import com.tafh.myfin_app.common.model.BaseEntity;
import com.tafh.myfin_app.user.model.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categories")
@Getter
@NoArgsConstructor
public class CategoryEntity extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private CategoryType type;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public static CategoryEntity create(UserEntity user, String name, CategoryType type) {
        if (user == null) {
            throw new DomainException("User is required");
        }

        if (name == null || name.isBlank()) {
            throw new DomainException("Category name is required");
        }

        if (type == null) {
            throw new DomainException("Category type is required");
        }

        CategoryEntity category = new CategoryEntity();
        category.name = name;
        category.type = type;
        category.user = user;
        category.active = true;

        return category;
    }

    public void update(String name, CategoryType type, Boolean active) {
        if (name == null || name.isBlank()) {
            throw new DomainException("Category name is required");
        }

        if (type == null) {
            throw new DomainException("Category type is required");
        }

        this.name = name;
        this.type = type;
        if (active != null) this.active = active;
    }

}
