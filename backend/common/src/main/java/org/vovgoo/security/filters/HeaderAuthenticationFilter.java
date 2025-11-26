package org.vovgoo.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vovgoo.dto.exception.ExceptionResponse;
import org.vovgoo.enums.user.UserStatus;
import org.vovgoo.user.client.UserClientService;
import org.vovgoo.user.exception.UserBlockedException;
import org.vovgoo.user.exception.UserDeactivatedException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private final UserClientService userClientService;
    private final ObjectMapper objectMapper;

    private static final List<String> DEACTIVATED_ALLOWED_PATHS = List.of(
            "/api/v1/users/me/reactivate"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String userId = request.getHeader("X-User-Id");
        String rolesHeader = request.getHeader("X-Roles");

        if (userId != null && !userId.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                validateUserStatus(UUID.fromString(userId), path);
                setAuthentication(userId, rolesHeader);
            } catch (UserBlockedException | UserDeactivatedException ex) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(objectMapper.writeValueAsString(ExceptionResponse.of(ex.getMessage(), HttpStatus.FORBIDDEN, path)));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validateUserStatus(UUID userId, String path) {
        UserStatus status = userClientService.getUser(userId).userStatus();

        if (status.equals(UserStatus.BLOCKED)) {
            throw new UserBlockedException();
        }

        if (status.equals(UserStatus.DEACTIVATED) &&
                DEACTIVATED_ALLOWED_PATHS.stream().noneMatch(path::startsWith)) {
            throw new UserDeactivatedException();
        }
    }

    private void setAuthentication(String userId, String rolesHeader) {
        List<SimpleGrantedAuthority> authorities = (rolesHeader != null && !rolesHeader.isEmpty())
                ? Arrays.stream(rolesHeader.split(","))
                .map(String::trim)
                .map(role -> "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
                : List.of();

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userId, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}