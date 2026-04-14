package com.tafh.myfin_app.category.service;

import com.tafh.myfin_app.category.dto.CategoryRequest;
import com.tafh.myfin_app.category.dto.CategoryResponse;
import com.tafh.myfin_app.category.mapper.CategoryMapper;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.model.CategoryType;
import com.tafh.myfin_app.category.repository.CategoryRepository;
import com.tafh.myfin_app.common.exception.ResourceNotFoundException;
import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.common.security.SecurityUtil;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        String userId = SecurityUtil.getCurrentUserId();

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        CategoryEntity category = CategoryEntity.create(
                user,
                request.getName(),
                request.getType()
        );

        return  categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll(CategoryType type) {
        String userId = SecurityUtil.getCurrentUserId();
        userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        List<CategoryEntity> categories = (type == null)
                ? categoryRepository.findByUserId(userId)
                : categoryRepository.findByUserIdAndType(userId, type);

        return categories
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getById(String id) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        CategoryEntity category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return categoryMapper.toCategoryResponse(category);
    }

    @Transactional
    public CategoryResponse update(String id, CategoryRequest request) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        CategoryEntity category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.update(
                request.getName(),
                request.getType()
        );

        return categoryMapper.toCategoryResponse(category);
    }

    @Transactional
    public void deleteById(String id) {
        String userId = SecurityUtil.getCurrentUserId();
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Unauthorized"));

        CategoryEntity category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }
}
