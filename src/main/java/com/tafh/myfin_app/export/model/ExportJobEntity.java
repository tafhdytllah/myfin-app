package com.tafh.myfin_app.export.model;

import com.tafh.myfin_app.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "export_jobs")
@Getter
@NoArgsConstructor
public class ExportJobEntity extends BaseEntity {

    @Column(length = 64, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String fileName;

    @Column(length = 500, nullable = false)
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "export_type")
    private ExportTypeEnum exportType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ExportStatusEnum status;

    @Column(length = 500)
    private String description;

    public static ExportJobEntity create(String userId, ExportTypeEnum exportType) {

        ExportJobEntity job = new ExportJobEntity();
        job.userId = userId;
        job.exportType = exportType;
        job.status = ExportStatusEnum.PENDING;

        return job;
    }

    public void markProcessing() {
        this.status = ExportStatusEnum.PROCESSING;
    }

    public void markDone(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.status = ExportStatusEnum.DONE;
    }

    public void markFailed(String description) {
        this.status = ExportStatusEnum.FAILED;
        this.description = description;
    }

}
