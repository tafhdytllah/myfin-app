package com.tafh.myfin_app.category.service;

import com.tafh.myfin_app.category.dto.CategoryRequest;
import com.tafh.myfin_app.category.dto.CategoryResponse;
import com.tafh.myfin_app.category.mapper.CategoryMapper;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.category.projection.CategoryProjection;
import com.tafh.myfin_app.category.repository.CategoryRepository;
import com.tafh.myfin_app.common.exception.ResourceNotFoundException;
import com.tafh.myfin_app.common.security.CurrentUser;
import com.tafh.myfin_app.common.util.LikeQueryHelper;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import com.tafh.myfin_app.user.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryMapper categoryMapper;
    private final CurrentUser currentUser;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        String userId = currentUser.getId();

        UserEntity userEntity = entityManager.getReference(UserEntity.class, userId);

        CategoryEntity categoryEntity = CategoryEntity.create(
                userEntity,
                request.getName().trim(),
                request.getType()
        );

        categoryRepository.save(categoryEntity);

        return categoryMapper.toCategoryResponse(categoryEntity, 0L);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategories(
            CategoryType type,
            String keyword,
            Pageable pageable
    ) {
        String userId = currentUser.getId();

        String searchTerm = LikeQueryHelper.toContainsPattern(keyword);

        Page<CategoryProjection> page = categoryRepository
                .findAllWithFilter(
                        userId,
                        type,
                        searchTerm,
                        pageable
                );

        return page.map(categoryMapper::toCategoryResponse);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategory(String id) {
        String userId = currentUser.getId();

        CategoryProjection categoryDetail = categoryRepository
                .findDetail(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));

        return categoryMapper.toCategoryResponse(categoryDetail);
    }

    @Transactional
    public CategoryResponse update(String id, CategoryRequest request) {
        String userId = currentUser.getId();

        CategoryEntity categoryEntity = categoryRepository
                .findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));

        categoryEntity.update(
                request.getName().trim(),
                request.getType(),
                request.getActive()
        );

        CategoryProjection categoryDetail = categoryRepository
                .findDetail(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));

        return categoryMapper.toCategoryResponse(categoryDetail);
    }

    @Transactional
    public void deleteById(String id) {
        String userId = currentUser.getId();

        CategoryEntity categoryEntity = categoryRepository
                .findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found"));

        categoryRepository.delete(categoryEntity);
    }
}
