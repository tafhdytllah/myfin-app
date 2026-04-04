package com.tafh.myfin_app.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogHelper {

    private static final ObjectMapper mapper = new ObjectMapper();

    // ---------- Controller / Request & Response ----------
    public static void request(String endpoint, Object request) {
        log.info("Request {}: {}", endpoint, toJsonString(request));
    }

    public static void response(String endpoint, Object response) {
        log.info("Response {}: {}", endpoint, toJsonString(response));
    }

    // ---------- Service / Business Logic ----------
    public static void info(String message, Object id) {
        log.info("{} | id: {}", message, id);
    }

    public static void debug(String message, Object id) {
        log.debug("{} | id: {}", message, id);
    }

    public static void warn(String message, Object id) {
        log.warn("{} | id: {}", message, id);
    }

    public static void error(String message, Object id, Exception e) {
        log.error("{} | id: {} | Exception: {}", message, id, e.getMessage());
    }

    // ---------- Global / any object ----------
    public static void error(String message, Exception e) {
        log.error("{} | Exception: {}", message, e.getMessage());
    }

    private static String toJsonString(Object obj) {
        try {
            if (obj == null) return "-";
            return  mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return "Failed to serialize object: " + e.getMessage();
        }
    }
}
