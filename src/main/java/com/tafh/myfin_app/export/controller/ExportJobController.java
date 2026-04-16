package com.tafh.myfin_app.export.controller;

import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.security.SecurityHelper;
import com.tafh.myfin_app.common.util.FileResponseHelper;
import com.tafh.myfin_app.common.util.ResponseHelper;
import com.tafh.myfin_app.export.dto.CreateExportResponse;
import com.tafh.myfin_app.export.dto.ExportRequest;
import com.tafh.myfin_app.export.dto.ExportResponse;
import com.tafh.myfin_app.export.service.ExportJobService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/exports")
@RequiredArgsConstructor
public class ExportJobController {

    private final ExportJobService exportJobService;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateExportResponse>> create(@RequestBody ExportRequest request) {
        String userId = SecurityHelper.getCurrentUserId();
        return ResponseHelper.ok(exportJobService.createExport(userId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExportResponse>> getStatus(@PathVariable String id
    ) {
        return ResponseHelper.ok(exportJobService.getStatus(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable String id) {

        File file = exportJobService.getFile(id);
        return FileResponseHelper.setAttachment(file);
    }
}
