package com.tafh.myfin_app.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryStatusRequest {

    @NotNull(message = "active is required")
    private Boolean active;

}
