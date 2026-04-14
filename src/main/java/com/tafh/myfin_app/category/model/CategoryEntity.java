package com.tafh.myfin_app.category.model;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public static CategoryEntity create(UserEntity user, String name, CategoryType type) {
        CategoryEntity category = new CategoryEntity();
        category.name = name;
        category.type = type;
        category.user = user;
        return category;
    }

    public void update(String name, CategoryType type) {
        this.name = name;
        this.type = type;
    }

}
