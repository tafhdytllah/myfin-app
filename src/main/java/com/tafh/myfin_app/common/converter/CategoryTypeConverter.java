package com.tafh.myfin_app.common.converter;

import com.tafh.myfin_app.category.model.CategoryType;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryTypeConverter implements Converter<String, CategoryType> {

    @Override
    public CategoryType convert(String source) {
        return CategoryType.valueOf(source.trim().toUpperCase());
    }
}
