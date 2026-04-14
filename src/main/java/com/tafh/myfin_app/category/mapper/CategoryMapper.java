package com.tafh.myfin_app.category.mapper;

import com.tafh.myfin_app.category.dto.CategoryResponse;
import com.tafh.myfin_app.category.model.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(CategoryEntity category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .build();
    }

}
