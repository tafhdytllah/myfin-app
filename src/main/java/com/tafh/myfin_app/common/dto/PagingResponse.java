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

    private int currentPage;
    private int pageSize;
    private int totalPages;

}
