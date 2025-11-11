package org.vovgoo.orderservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long getCurrentUserId() {
        Authentication auth = getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Пользователь не аутентифицирован");
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof Jwt jwt) {
            return jwt.getClaim("userId");
        } else {
            throw new RuntimeException("Невозможно извлечь userId из principal");
        }
    }

    public static boolean isAdmin() {
        Authentication auth = getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return roles.contains("ROLE_ADMIN");
    }

    public static boolean isUser() {
        Authentication auth = getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return false;

        Set<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return roles.contains("ROLE_USER");
    }
}
