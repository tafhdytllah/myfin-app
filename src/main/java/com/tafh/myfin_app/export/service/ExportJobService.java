package com.tafh.myfin_app.export.service;

import com.tafh.myfin_app.common.exception.ResourceNotFoundException;
import com.tafh.myfin_app.common.security.CurrentUser;
import com.tafh.myfin_app.common.util.LogHelper;
import com.tafh.myfin_app.export.dto.CreateExportResponse;
import com.tafh.myfin_app.export.dto.ExportRequest;
import com.tafh.myfin_app.export.dto.ExportResponse;
import com.tafh.myfin_app.export.mapper.ExportMapper;
import com.tafh.myfin_app.export.model.ExportJobEntity;
import com.tafh.myfin_app.export.model.ExportStatusEnum;
import com.tafh.myfin_app.export.model.ExportTypeEnum;
import com.tafh.myfin_app.export.repository.ExportJobRepository;
import com.tafh.myfin_app.transaction.model.TransactionEntity;
import com.tafh.myfin_app.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExportJobService {

    private final ExportJobRepository exportJobRepository;
    private final TransactionRepository transactionRepository;
    private final ExcelExportService excelExportService;
    private final PdfExportService pdfExportService;
    private final ExportMapper exportMapper;
    private final CurrentUser currentUser;

    public CreateExportResponse createExport(ExportRequest request) {
        String userId = currentUser.getId();

        ExportJobEntity job = ExportJobEntity.create(userId, request.getExportType());
        exportJobRepository.save(job);

        generateFileAsync(job.getId(), userId);

        return CreateExportResponse.builder()
                .id(job.getId())
                .build();
    }

    public ExportResponse getStatus(String id) {
        ExportJobEntity exportJob = exportJobRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Export not found"));

        return exportMapper.toExportResponse(exportJob);
    }

    @Async
    public void generateFileAsync(String id, String userId) {

        ExportJobEntity job = exportJobRepository.findById(id).orElseThrow();

        try {
            job.markProcessing();
            exportJobRepository.save(job);

            List<TransactionEntity> data = transactionRepository.findAllByUserIdForExport(userId);

            ByteArrayInputStream file;
            String extension;

            if (job.getExportType() == ExportTypeEnum.PDF) {
                file = pdfExportService.buildPdf(data);
                extension = ".pdf";
            } else {
                file = excelExportService.buildExcel(data);
                extension = ".xlsx";
            }

            Files.createDirectories(Paths.get("storage"));

            String fileName = "transactions_"
                    + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                    + UUID.randomUUID().toString().substring(0, 4)
                    + extension;

            String path = "storage/" + fileName;

            Files.copy(file, Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

            job.markDone(fileName, path);

        } catch (Exception e) {
            job.markFailed("Failed to generate export job");
            LogHelper.error("Export failed for jobId= {}", id, e.getMessage());
        }

        exportJobRepository.save(job);
    }

    public File getFile(String id) {

        String userId = currentUser.getId();

        ExportJobEntity job = exportJobRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Export not found"));

        if (job.getStatus() != ExportStatusEnum.DONE) {
            throw new RuntimeException("File not ready");
        }

        File file = new File(job.getFilePath());

        if (!file.exists()) {
            throw new ResourceNotFoundException("File not found");
        }

        return file;
    }

}
