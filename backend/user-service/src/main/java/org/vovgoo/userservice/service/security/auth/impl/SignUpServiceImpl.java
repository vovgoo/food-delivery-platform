package org.vovgoo.userservice.service.security.auth.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.enums.user.RoleType;
import org.vovgoo.enums.user.UserStatus;
import org.vovgoo.userservice.config.verification.VerificationProperty;
import org.vovgoo.userservice.domain.redis.signup.SignUpAttemptsKey;
import org.vovgoo.userservice.domain.redis.signup.SignUpCodeKey;
import org.vovgoo.userservice.domain.redis.signup.SignUpRequestKey;
import org.vovgoo.userservice.dto.security.auth.request.ConfirmSignUpRequest;
import org.vovgoo.userservice.dto.security.auth.request.SignUpRequest;
import org.vovgoo.userservice.dto.security.jwt.internal.JwtPair;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.custom.role.RoleNotFoundException;
import org.vovgoo.userservice.exception.custom.user.PhoneAlreadyExistsException;
import org.vovgoo.userservice.exception.custom.verification.InvalidOtpException;
import org.vovgoo.userservice.exception.custom.verification.OtpAttemptsExceededException;
import org.vovgoo.userservice.exception.custom.verification.OtpNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.SignUpRequestNotFoundException;
import org.vovgoo.userservice.repository.RoleRepository;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.rabbit.EventService;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.security.auth.SignUpService;
import org.vovgoo.userservice.service.security.jwt.JwtTokenProvider;
import org.vovgoo.userservice.service.security.jwt.enums.JwtTokenType;
import org.vovgoo.userservice.utils.VerificationUtils;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final RedisService redisService;
    private final EventService eventService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationProperty verificationProperty;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void signUp(SignUpRequest request) {
        userRepository.findByPhone(request.phone())
                .ifPresent(u -> { throw new PhoneAlreadyExistsException(request.phone()); });

        String phone = request.phone();
        String otp = VerificationUtils.generateOtp();

        String encodedPassword = passwordEncoder.encode(request.password());

        SignUpRequest safeRequest = SignUpRequest.builder()
                .phone(phone)
                .fullName(request.fullName())
                .birthDate(request.birthDate())
                .password(encodedPassword)
                .build();

        redisService.set(SignUpRequestKey.of(phone), safeRequest);
        redisService.set(SignUpCodeKey.of(phone), otp);
        redisService.set(SignUpAttemptsKey.of(phone), 0);

        eventService.publishSignUpEvent(phone, otp);
    }

    @Override
    @Transactional
    public JwtPair confirmSignUp(ConfirmSignUpRequest request) {
        String phone = request.phone();
        String providedCode = request.code();

        SignUpRequest signUpRequest = redisService.get(SignUpRequestKey.of(phone))
                .orElseThrow(SignUpRequestNotFoundException::new);

        String actualOtp = redisService.get(SignUpCodeKey.of(phone))
                .orElseThrow(OtpNotFoundException::new);

        AtomicInteger attempts = new AtomicInteger(redisService.get(SignUpAttemptsKey.of(phone)).orElse(0));

        boolean valid = VerificationUtils.verifyToken(
                actualOtp,
                providedCode,
                attempts,
                verificationProperty.getAttempts().getPhone()
        );

        redisService.set(SignUpAttemptsKey.of(phone), attempts.get());

        if (!valid) {
            if (attempts.get() >= verificationProperty.getAttempts().getPhone()) {
                redisService.delete(SignUpRequestKey.of(phone));
                redisService.delete(SignUpCodeKey.of(phone));
                redisService.delete(SignUpAttemptsKey.of(phone));
                throw new OtpAttemptsExceededException();
            }

            throw new InvalidOtpException();
        }

        redisService.delete(SignUpRequestKey.of(phone));
        redisService.delete(SignUpCodeKey.of(phone));
        redisService.delete(SignUpAttemptsKey.of(phone));

        Role role = roleRepository.findByName(RoleType.USER)
                .orElseThrow(RoleNotFoundException::new);

        User user = User.builder()
                .phone(signUpRequest.phone())
                .fullName(signUpRequest.fullName())
                .birthDate(signUpRequest.birthDate())
                .status(UserStatus.ACTIVE)
                .passwordHash(signUpRequest.password())
                .roles(Set.of(role))
                .build();

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new PhoneAlreadyExistsException(signUpRequest.phone());
        }

        return JwtPair.builder()
                .accessToken(jwtTokenProvider.generateToken(JwtTokenType.ACCESS, user))
                .refreshToken(jwtTokenProvider.generateToken(JwtTokenType.REFRESH, user))
                .build();
    }
}
