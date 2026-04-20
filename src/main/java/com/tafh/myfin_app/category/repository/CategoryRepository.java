package com.tafh.myfin_app.category.repository;

import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {

    Optional<CategoryEntity> findByIdAndUser_Id(String id, String userId);

    @Query("""
                SELECT c
                FROM CategoryEntity c
                WHERE c.user.id = :userId
                  AND (
                        :type IS NULL OR c.type = :type
                      )
                  AND (
                        :searchTerm IS NULL
                        OR :searchTerm = ''
                        OR LOWER(c.name) LIKE :searchTerm ESCAPE '\\'
                      )
            """)
    Page<CategoryEntity> findAllWithFilter(
            @Param("userId") String userId,
            @Param("type") CategoryType type,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

}
