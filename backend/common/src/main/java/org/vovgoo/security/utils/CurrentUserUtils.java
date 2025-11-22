package org.vovgoo.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vovgoo.enums.user.RoleType;

import java.util.Collection;
import java.util.UUID;

public class CurrentUserUtils {

    public static UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }
        try {
            return UUID.fromString(auth.getName());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Некорректный UUID пользователя", e);
        }
    }

    public static boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(RoleType.ADMIN.getAuthority()));
    }
}
