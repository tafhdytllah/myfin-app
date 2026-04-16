package com.tafh.myfin_app.export.mapper;

import com.tafh.myfin_app.export.dto.ExportResponse;
import com.tafh.myfin_app.export.model.ExportJobEntity;
import com.tafh.myfin_app.export.model.ExportStatusEnum;
import org.springframework.stereotype.Component;

@Component
public class ExportMapper {

    public ExportResponse toExportResponse(ExportJobEntity exportJob) {
        String downloadUrl = "/api/v1/exports/%s/download".formatted(exportJob.getId());

        return ExportResponse.builder()
                .id(exportJob.getId())
                .status(exportJob.getStatus().name())
                .fileName(exportJob.getFileName())
                .downloadUrl(
                        exportJob.getStatus() == ExportStatusEnum.DONE
                                ? downloadUrl
                                : null
                )
                .createdAt(exportJob.getCreatedAt())
                .build();

    }
}
