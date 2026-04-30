package com.tafh.myfin_app.category.dto;

import com.tafh.myfin_app.category.model.CategoryType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private String id;
    private String name;
    private CategoryType type;
    private boolean active;
    private long usageCount;
}
