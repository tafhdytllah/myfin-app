package com.tafh.myfin_app.category.repository;

import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

    Optional<CategoryEntity> findByIdAndUserId(String id, String userId);

    List<CategoryEntity> findByUserId(String userId);

    List<CategoryEntity> findByUserIdAndType(String userId, CategoryType type);

}
