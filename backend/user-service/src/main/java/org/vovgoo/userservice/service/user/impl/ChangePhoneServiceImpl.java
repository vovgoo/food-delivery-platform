package org.vovgoo.userservice.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.security.utils.CurrentUserUtils;
import org.vovgoo.user.aspect.CheckUserStatus;
import org.vovgoo.userservice.config.verification.VerificationProperty;
import org.vovgoo.userservice.domain.redis.phone.change.PhoneChangeAttemptsKey;
import org.vovgoo.userservice.domain.redis.phone.change.PhoneChangeCodeKey;
import org.vovgoo.userservice.domain.redis.phone.change.PhoneChangeRequestKey;
import org.vovgoo.userservice.dto.user.request.ChangePhoneRequest;
import org.vovgoo.userservice.dto.user.request.ConfirmChangePhoneRequest;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.custom.user.PhoneAlreadyExistsException;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.ChangePhoneRequestNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.InvalidOtpException;
import org.vovgoo.userservice.exception.custom.verification.OtpAttemptsExceededException;
import org.vovgoo.userservice.exception.custom.verification.OtpNotFoundException;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.rabbit.EventService;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.user.ChangePhoneService;
import org.vovgoo.userservice.utils.VerificationUtils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ChangePhoneServiceImpl implements ChangePhoneService {

    private final RedisService redisService;
    private final EventService eventService;
    private final UserRepository userRepository;
    private final VerificationProperty verificationProperty;

    @Override
    @CheckUserStatus
    public void changePhone(ChangePhoneRequest request) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        userRepository.findByPhone(request.phone())
                .ifPresent( u -> {throw new PhoneAlreadyExistsException(request.phone()); });

        String phone = request.phone();
        String otp = VerificationUtils.generateOtp();

        redisService.set(PhoneChangeRequestKey.of(userId), request);
        redisService.set(PhoneChangeCodeKey.of(userId), otp);
        redisService.set(PhoneChangeAttemptsKey.of(userId), 0);

        eventService.publishPhoneChangeEvent(phone, otp);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void confirmChangePhone(ConfirmChangePhoneRequest request) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(UserNotFoundException::new);

        ChangePhoneRequest changePhoneRequest = redisService.get(PhoneChangeRequestKey.of(userId))
                .orElseThrow(ChangePhoneRequestNotFoundException::new);

        String code = request.code();

        String actualOtp = redisService.get(PhoneChangeCodeKey.of(userId))
                .orElseThrow(OtpNotFoundException::new);

        AtomicInteger attempts = new AtomicInteger(redisService.get(PhoneChangeAttemptsKey.of(userId)).orElse(0));

        boolean valid = VerificationUtils.verifyToken(
                actualOtp,
                code,
                attempts,
                verificationProperty.getAttempts().getPhone()
        );

        redisService.set(PhoneChangeAttemptsKey.of(userId), attempts.get());

        if (!valid) {
            redisService.delete(PhoneChangeRequestKey.of(userId));
            redisService.delete(PhoneChangeCodeKey.of(userId));
            redisService.delete(PhoneChangeAttemptsKey.of(userId));

            if (attempts.get() >= verificationProperty.getAttempts().getPhone()) {
                throw new OtpAttemptsExceededException();
            }

            throw new InvalidOtpException();
        }

        redisService.delete(PhoneChangeRequestKey.of(userId));
        redisService.delete(PhoneChangeCodeKey.of(userId));
        redisService.delete(PhoneChangeAttemptsKey.of(userId));

        userRepository.findByPhone(changePhoneRequest.phone())
                .ifPresent(u -> { throw new PhoneAlreadyExistsException(changePhoneRequest.phone()); });

        user.setPhone(changePhoneRequest.phone());
        userRepository.save(user);
    }
}
