package org.vovgoo.userservice.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CookieUtilsTest {

    @Test
    void addRefreshTokenCookie_shouldAddCookie() {
        HttpServletResponse response = mock(HttpServletResponse.class);
        String token = "refresh-token-123";

        CookieUtils.addRefreshTokenCookie(response, token);

        verify(response).addCookie(argThat(cookie ->
                "refreshToken".equals(cookie.getName()) &&
                        token.equals(cookie.getValue()) &&
                        cookie.isHttpOnly() &&
                        cookie.getSecure() &&
                        "/".equals(cookie.getPath()) &&
                        cookie.getMaxAge() == 15 * 24 * 60 * 60
        ));
    }

    @Test
    void extractRefreshToken_shouldReturnTokenIfPresent() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("refreshToken", "token123");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String result = CookieUtils.extractRefreshToken(request);

        assertEquals("token123", result);
    }

    @Test
    void extractRefreshToken_shouldReturnNullIfNoCookies() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        String result = CookieUtils.extractRefreshToken(request);

        assertNull(result);
    }

    @Test
    void extractRefreshToken_shouldReturnNullIfTokenMissing() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("otherCookie", "abc");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String result = CookieUtils.extractRefreshToken(request);

        assertNull(result);
    }
}
