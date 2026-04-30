package com.tafh.myfin_app.category.mapper;

import com.tafh.myfin_app.category.dto.CategoryResponse;
import com.tafh.myfin_app.category.model.CategoryEntity;
import com.tafh.myfin_app.category.projection.CategoryProjection;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(CategoryEntity category, long usageCount) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .active(category.isActive())
                .usageCount(usageCount)
                .build();
    }

    public CategoryResponse toCategoryResponse(CategoryProjection category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .active(category.getActive())
                .usageCount(category.getUsageCount())
                .build();
    }

}
