package org.vovgoo.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.vovgoo.security.property.InternalServiceTokens;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InternalHeaderAuthenticationFilter extends OncePerRequestFilter {

    private final InternalServiceTokens internalServiceTokens;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String service = request.getHeader("X-Service-Name");
        String token = request.getHeader("X-Internal-Token");

        if (service != null && !service.isEmpty() &&  token != null && !token.isEmpty()) {
            if (internalServiceTokens.getSelf().equals(token)) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(service, null,
                                List.of(new SimpleGrantedAuthority("ROLE_INTERNAL")));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
