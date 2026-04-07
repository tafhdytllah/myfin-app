package com.tafh.myfin_app.common.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogHelper {

    public static void info(String message, Object... args) {
        log.info(message, args);
    }

    public static void debug(String message, Object... args) {
        log.debug(message, args);
    }

    public static void warn(String message, Object... args) {
        log.warn(message, args);
    }

    public static void error(String message, Object... arg) {
        log.error(message, arg);
    }

}
