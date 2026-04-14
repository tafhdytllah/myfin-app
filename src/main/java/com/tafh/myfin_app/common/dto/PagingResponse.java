package com.tafh.myfin_app.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {

    private int page;

    private int size;

    private int totalPages;

    private long totalElements;

    private boolean hasNext;

    private boolean hasPrevious;
}
