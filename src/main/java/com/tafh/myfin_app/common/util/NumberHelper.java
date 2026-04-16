package com.tafh.myfin_app.common.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class NumberHelper {

    public static BigDecimal zeroIfNull(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

}
