package com.tafh.myfin_app.account.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusAccountRequest {

    @NotNull(message = "active is required")
    private Boolean active;

}
