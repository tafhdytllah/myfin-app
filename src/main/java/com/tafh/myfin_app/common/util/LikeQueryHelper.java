package com.tafh.myfin_app.common.util;

import org.springframework.stereotype.Component;

@Component
public class LikeQueryHelper {

    public static String toContainsPattern(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return null;
        }

        String normalized = keyword.toLowerCase().trim();

        /**
         * Escape special characters for LIKE
          */
        normalized = normalized
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");

        return "%" + normalized + "%";
    }

}
