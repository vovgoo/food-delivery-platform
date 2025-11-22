package org.vovgoo.userservice.service.security.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.enums.user.UserStatus;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.config.security.SecurityConfig;
import org.vovgoo.userservice.dto.security.auth.request.*;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;
import org.vovgoo.userservice.dto.security.jwt.response.JwtResponse;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.enums.user.RoleType;
import org.vovgoo.userservice.exception.custom.role.RoleNotFoundException;
import org.vovgoo.userservice.exception.custom.security.InvalidRefreshTokenException;
import org.vovgoo.userservice.exception.custom.user.PhoneAlreadyExistsException;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.SignUpRequestNotFoundException;
import org.vovgoo.userservice.repository.RoleRepository;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.security.auth.AuthService;
import org.vovgoo.userservice.service.security.jwt.JwtTokenProvider;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;
import org.vovgoo.userservice.service.verification.phone.PhoneVerificationService;
import org.vovgoo.userservice.service.verification.phone.enums.PhoneVerificationType;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RedisService redisService;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final PhoneVerificationService phoneVerificationService;

    @Override
    public JwtPair signIn(SignInRequest signInRequest) {
        User user = userRepository.findByPhone(signInRequest.phone()).orElse(null);

        String hashToCheck = (user != null)
                ? user.getPasswordHash()
                : SecurityConfig.DUMMY_PASSWORD_HASH;

        boolean passwordMatches = passwordEncoder.matches(signInRequest.password(), hashToCheck);

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
    public void signUp(SignUpRequest signUpRequest) {
        userRepository.findByPhone(signUpRequest.phone())
                .ifPresent(u -> { throw new PhoneAlreadyExistsException(signUpRequest.phone()); });

        phoneVerificationService.send(signUpRequest.phone(), PhoneVerificationType.SIGN_UP);

        redisService.set(RedisKey.SIGNUP_REQUEST, signUpRequest, signUpRequest.phone());
    }

    @Override
    @Transactional
    public JwtPair confirmSignUp(ConfirmSignUpRequest confirmSignUpRequest) {
        String phone = confirmSignUpRequest.phone();
        String code = confirmSignUpRequest.code();

        SignUpRequest signUpRequest = redisService.get(RedisKey.SIGNUP_REQUEST, SignUpRequest.class, phone)
                .orElseThrow(SignUpRequestNotFoundException::new);

        phoneVerificationService.validate(phone, code, PhoneVerificationType.SIGN_UP);

        Role role = roleRepository.findByName(RoleType.USER)
                .orElseThrow(RoleNotFoundException::new);

        User user = User.builder()
                .phone(signUpRequest.phone())
                .fullName(signUpRequest.fullName())
                .birthDate(signUpRequest.birthDate())
                .status(UserStatus.ACTIVE)
                .passwordHash(passwordEncoder.encode(signUpRequest.password()))
                .roles(Set.of(role))
                .build();

        user = userRepository.save(user);

        redisService.delete(RedisKey.SIGNUP_REQUEST, phone);

        return JwtPair.builder()
                .accessToken(jwtTokenProvider.generateToken(JwtTokenType.ACCESS, user))
                .refreshToken(jwtTokenProvider.generateToken(JwtTokenType.REFRESH, user))
                .build();
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
