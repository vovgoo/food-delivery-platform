package org.vovgoo.userservice.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {

    private static final int REFRESH_TOKEN_MAX_AGE_SECONDS = 15 * 24 * 60 * 60;

    public static void addRefreshTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(REFRESH_TOKEN_MAX_AGE_SECONDS);

        String cookieHeader = cookie.getName() + "=" + cookie.getValue() +
                "; Max-Age=" + cookie.getMaxAge() +
                "; Path=" + cookie.getPath() +
                "; HttpOnly" +
                "; Secure" +
                "; SameSite=Strict";

        response.addHeader("Set-Cookie", cookieHeader);
    }

    public static String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }
}
