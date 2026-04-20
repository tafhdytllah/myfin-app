package com.tafh.myfin_app.export.dto;

import com.tafh.myfin_app.export.model.ExportTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExportRequest {

    @NotNull(message = "Export type is required")
    private ExportTypeEnum exportType;

}
