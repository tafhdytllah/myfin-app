package com.tafh.myfin_app.account.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequest {

    @NotBlank(message = "Name is required")
    private String name;

}
