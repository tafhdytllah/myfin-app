package com.tafh.myfin_app.common.security;

import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.user.model.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static String getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new UnauthorizedException("Unauthorized");
        }

        Object principal = auth.getPrincipal();

        if (principal instanceof String userId) {
            if (userId.equals("anonymousUser")) {
                throw new UnauthorizedException("Unauthorized");
            }
            return userId;
        }

        if (principal instanceof UserEntity user) {
            return user.getId();
        }

        throw new UnauthorizedException("Invalid authentication");
    }
}
