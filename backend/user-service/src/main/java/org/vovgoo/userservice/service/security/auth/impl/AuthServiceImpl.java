package org.vovgoo.userservice.service.security.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.common.domain.user.enums.UserStatus;
import org.vovgoo.userservice.config.security.SecurityConfig;
import org.vovgoo.userservice.dto.security.auth.request.*;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;
import org.vovgoo.userservice.dto.security.jwt.response.JwtResponse;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.custom.auth.InvalidRefreshTokenException;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.security.auth.AuthService;
import org.vovgoo.userservice.service.security.jwt.JwtTokenProvider;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtPair signIn(SignInRequest request) {
        User user = userRepository.findByPhoneWithRoles(request.phone()).orElse(null);

        String hashToCheck = (user != null)
                ? user.getPasswordHash()
                : SecurityConfig.DUMMY_PASSWORD_HASH;

        boolean passwordMatches = passwordEncoder.matches(request.password(), hashToCheck);

        if (!passwordMatches) {
            throw new BadCredentialsException("Неверный номер или пароль");
        }

        if (user == null) {
            throw new BadCredentialsException("Неверный номер или пароль");
        }

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new BadCredentialsException("Пользователь заблокирован");
        }

        return new JwtPair(
                jwtTokenProvider.generateToken(JwtTokenType.ACCESS, user),
                jwtTokenProvider.generateToken(JwtTokenType.REFRESH, user)
        );
    }

    @Override
    public JwtResponse refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(JwtTokenType.REFRESH, refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        UUID userId = jwtTokenProvider.extractUserId(JwtTokenType.REFRESH, refreshToken);

        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(UserNotFoundException::new);

        String newAccessToken = jwtTokenProvider.generateToken(JwtTokenType.ACCESS, user);

        return JwtResponse.builder()
                .accessToken(newAccessToken)
                .build();
    }
}
