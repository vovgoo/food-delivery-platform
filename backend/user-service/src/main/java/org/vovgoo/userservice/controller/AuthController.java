package org.vovgoo.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vovgoo.userservice.dto.auth.request.LoginRequest;
import org.vovgoo.userservice.dto.auth.request.RegisterRequest;
import org.vovgoo.userservice.dto.auth.response.JwtResponse;
import org.vovgoo.userservice.dto.auth.internal.JwtPair;
import org.vovgoo.userservice.service.security.AuthService;
import org.vovgoo.userservice.utils.CookieUtils;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest registerRequest, HttpServletResponse response) {
        JwtPair jwtPair = authService.register(registerRequest);
        CookieUtils.addRefreshTokenCookie(response, jwtPair.refreshToken());
        return ResponseEntity.ok(new JwtResponse(jwtPair.accessToken()));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        JwtPair jwtPair = authService.login(loginRequest);
        CookieUtils.addRefreshTokenCookie(response, jwtPair.refreshToken());
        return ResponseEntity.ok(new JwtResponse(jwtPair.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtils.extractRefreshToken(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        JwtPair jwtPair = authService.refreshAccessToken(refreshToken);
        CookieUtils.addRefreshTokenCookie(response, jwtPair.refreshToken());
        return ResponseEntity.ok(new JwtResponse(jwtPair.accessToken()));
    }
}
