package org.vovgoo.userservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vovgoo.userservice.dto.security.auth.request.*;
import org.vovgoo.userservice.dto.security.jwt.response.JwtResponse;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;
import org.vovgoo.userservice.dto.verification.response.PhoneVerificationResponse;
import org.vovgoo.userservice.service.security.auth.AuthService;
import org.vovgoo.userservice.utils.CookieUtils;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "Регистрация, логин и обновление токена")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<JwtResponse> signIn(@Valid @RequestBody SignInRequest signInRequest, HttpServletResponse response) {
        JwtPair jwtPair = authService.signIn(signInRequest);
        CookieUtils.addRefreshTokenCookie(response, jwtPair.refreshToken());
        return ResponseEntity.ok(new JwtResponse(jwtPair.accessToken()));
    }

    @PostMapping("/signUp")
    public ResponseEntity<PhoneVerificationResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/resendOtpCode")
    public ResponseEntity<Void> resendSignUpOtpCode(@RequestParam("token") String token) {
        authService.resendSignUpOtpCode(token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/confirmSignUp")
    public ResponseEntity<JwtResponse> confirmSignUp(@RequestParam("token") String token, @Valid @RequestBody ConfirmSignUpRequest confirmSignUpRequest, HttpServletResponse response) {
        JwtPair jwtPair = authService.confirmSignUp(token, confirmSignUpRequest);
        CookieUtils.addRefreshTokenCookie(response, jwtPair.refreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new JwtResponse(jwtPair.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = CookieUtils.extractRefreshToken(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        JwtResponse jwtResponse = authService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(jwtResponse);
    }
}
