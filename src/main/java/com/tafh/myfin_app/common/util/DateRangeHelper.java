package com.tafh.myfin_app.common.util;

import com.tafh.myfin_app.common.model.DateTimeRange;
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

        return DateTimeRange.builder()
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }

    public static int resolveYearOrCurrent(Integer year) {
        return (year != null) ? year : LocalDateTime.now().getYear();
    }

}
