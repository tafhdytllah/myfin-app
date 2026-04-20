package com.tafh.myfin_app.export.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportResponse {

    private String id;
    private String status;
    private String fileName;
    private String downloadUrl;
    private LocalDateTime createdAt;
}
