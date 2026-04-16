package com.tafh.myfin_app.common.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class DateRangeHelper {

    public static DateTimeRange toDateTimeRange(
            LocalDate startDate,
            LocalDate endDate
    ) {
        LocalDateTime startDateTime = (startDate != null)
                ? startDate.atStartOfDay()
                : LocalDateTime.of(1970, 1, 1, 0, 0);

        LocalDateTime endDateTime = (endDate != null)
                ? endDate.atTime(LocalTime.MAX)
                : LocalDateTime.now();

        return new DateTimeRange(startDateTime, endDateTime);
    }

    public record DateTimeRange(
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {}

}
