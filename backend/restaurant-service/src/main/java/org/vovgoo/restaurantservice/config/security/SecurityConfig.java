package org.vovgoo.restaurantservice.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.vovgoo.security.filters.HeaderAuthenticationFilter;
import org.vovgoo.security.filters.InternalHeaderAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final HeaderAuthenticationFilter headerAuthenticationFilter;
    private final InternalHeaderAuthenticationFilter internalHeaderAuthenticationFilter;

    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/restaurants", "/api/v1/restaurants/**").permitAll()
                        .requestMatchers(
                                "/api/v1/restaurants",
                                "/api/v1/restaurants/{restaurantId}",
                                "/api/v1/restaurants/{restaurantId}/dishes",
                                "/api/v1/restaurants/{restaurantId}/dishes/{dishId}",
                                "/api/v1/restaurants/{restaurantId}/images",
                                "/api/v1/restaurants/{restaurantId}/images/{imageId}",
                                "/api/v1/restaurants/{restaurantId}/images/profile",
                                "/api/v1/restaurants/{restaurantId}/dishes/{dishId}/images",
                                "/api/v1/restaurants/{restaurantId}/dishes/{dishId}/profile"
                        ).hasRole("ADMIN")
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/internal/**").hasRole("INTERNAL")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(internalHeaderAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/restaurants", "/api/v1/restaurants/**").permitAll()
                        .requestMatchers(
                                "/api/v1/restaurants",
                                "/api/v1/restaurants/{restaurantId}",
                                "/api/v1/restaurants/{restaurantId}/dishes",
                                "/api/v1/restaurants/{restaurantId}/dishes/{dishId}",
                                "/api/v1/restaurants/{restaurantId}/images",
                                "/api/v1/restaurants/{restaurantId}/images/{imageId}",
                                "/api/v1/restaurants/{restaurantId}/images/profile",
                                "/api/v1/restaurants/{restaurantId}/dishes/{dishId}/images",
                                "/api/v1/restaurants/{restaurantId}/dishes/{dishId}/profile"
                        ).hasRole("ADMIN")
                        .requestMatchers("/internal/**").hasRole("INTERNAL")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(internalHeaderAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}