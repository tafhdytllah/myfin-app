package com.tafh.myfin_app.export.dto;

import com.tafh.myfin_app.export.model.ExportTypeEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportRequest {

    @NotBlank(message = "Export type is required")
    private ExportTypeEnum exportType;

}
