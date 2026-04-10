package com.tafh.myfin_app.common.cookie;

import com.tafh.myfin_app.config.properties.CookieProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieUtil {

    private final CookieProperties cookieProperties;

    public void addRefreshTokenCookie(HttpServletResponse response, String rawRefreshToken) {
        String cookieValue = "refreshToken=" + rawRefreshToken +
                "; HttpOnly" +
                (cookieProperties.isSecure() ? "; Secure" : "" ) +
                "; Path=/" +
                "; Max-Age=" + (30 * 24 * 60 * 60) +
                "; SameSite=Strict";

        response.addHeader("Set-Cookie", cookieValue);
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        String cookieValue = "refreshToken=" +
                "; HttpOnly" +
                (cookieProperties.isSecure() ? "; Secure" : "" ) +
                "; Path=/" +
                "; Max-Age=0" +
                "; SameSite=Strict";

        response.addHeader("Set-Cookie", cookieValue);
    }
}
