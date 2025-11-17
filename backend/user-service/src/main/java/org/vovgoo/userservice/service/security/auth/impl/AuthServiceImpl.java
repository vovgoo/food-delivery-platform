package org.vovgoo.userservice.service.security.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.userservice.config.redis.RedisKey;
import org.vovgoo.userservice.dto.security.auth.request.*;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;
import org.vovgoo.userservice.dto.security.auth.response.PhoneVerificationResponse;
import org.vovgoo.userservice.dto.security.jwt.response.JwtResponse;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.enums.RoleType;
import org.vovgoo.userservice.entity.enums.UserStatus;
import org.vovgoo.userservice.exception.custom.*;
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
        User user = userRepository.findByPhone(signInRequest.phone())
                .orElseThrow(() -> new BadCredentialsException("Неверный номер или телефон"));

        if (!passwordEncoder.matches(signInRequest.password(), user.getPasswordHash())) {
            throw new BadCredentialsException("Неверный номер или телефон");
        }

        return new JwtPair(
                jwtTokenProvider.generateToken(JwtTokenType.ACCESS, user),
                jwtTokenProvider.generateToken(JwtTokenType.REFRESH, user)
        );
    }

    @Override
    public PhoneVerificationResponse signUp(SignUpRequest signUpRequest) {
        userRepository.findByPhone(signUpRequest.phone())
                .ifPresent(u -> { throw new PhoneAlreadyExistsException(signUpRequest.phone()); });

        String token = phoneVerificationService.sendOtp(signUpRequest.phone(), PhoneVerificationType.SIGN_UP);

        redisService.set(RedisKey.SIGNUP_REQUEST, signUpRequest, token);

        return PhoneVerificationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public void resendSignUpOtpCode(String token) {
        SignUpRequest signUpRequest = redisService.get(RedisKey.SIGNUP_REQUEST, SignUpRequest.class, token)
                .orElseThrow(SignUpRequestNotFoundException::new);

        phoneVerificationService.sendOtp(signUpRequest.phone(), PhoneVerificationType.SIGN_UP);
    }

    @Override
    @Transactional
    public JwtPair confirmSignUp(String token, ConfirmSignUpRequest confirmSignUpRequest) {
        String code = confirmSignUpRequest.code();

        SignUpRequest signUpRequest = redisService.get(RedisKey.SIGNUP_REQUEST, SignUpRequest.class, token)
                .orElseThrow(SignUpRequestNotFoundException::new);

        phoneVerificationService.validateOtp(token, code, PhoneVerificationType.SIGN_UP);

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

        redisService.delete(RedisKey.SIGNUP_REQUEST, token);

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

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        String newAccessToken = jwtTokenProvider.generateToken(JwtTokenType.ACCESS, user);

        return JwtResponse.builder()
                .accessToken(newAccessToken)
                .build();

    }
}
