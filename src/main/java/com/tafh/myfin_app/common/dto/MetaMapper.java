package com.tafh.myfin_app.common.dto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MetaMapper {

    public MetaResponse buildMetaResponse(Page<?> page) {
        return MetaResponse.builder()
                .page(page.getNumber())
                .size(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }

}
