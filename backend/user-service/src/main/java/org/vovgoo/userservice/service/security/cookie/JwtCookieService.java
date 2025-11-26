package org.vovgoo.userservice.service.security.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.userservice.config.security.property.JwtExpirationProperty;

@Service
@RequiredArgsConstructor
public class JwtCookieService {

    private final JwtExpirationProperty jwtExpirationProperty;

    public void addRefreshToken(HttpServletResponse response, String refreshToken) {
        int maxAgeSeconds = (int) (jwtExpirationProperty.getRefreshMs() / 1000);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeSeconds);
        response.addHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue() +
                "; Max-Age=" + cookie.getMaxAge() +
                "; Path=" + cookie.getPath() +
                "; HttpOnly" +
                "; Secure" +
                "; SameSite=Strict");
    }

    public String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) return cookie.getValue();
        }
        return null;
    }
}