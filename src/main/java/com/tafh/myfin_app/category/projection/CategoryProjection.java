package com.tafh.myfin_app.category.projection;

import com.tafh.myfin_app.category.model.CategoryType;

public interface CategoryProjection {

    String getId();
    String getName();
    CategoryType getType();
    boolean getActive();
    long getUsageCount();
}
