package com.tafh.myfin_app.common.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DateTimeRange {

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
