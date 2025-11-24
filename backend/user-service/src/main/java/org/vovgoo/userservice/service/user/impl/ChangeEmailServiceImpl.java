package org.vovgoo.userservice.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.security.utils.CurrentUserUtils;
import org.vovgoo.user.aspect.CheckUserStatus;
import org.vovgoo.userservice.config.verification.VerificationProperty;
import org.vovgoo.userservice.domain.redis.email.change.EmailChangeAttemptsKey;
import org.vovgoo.userservice.domain.redis.email.change.EmailChangeRequestKey;
import org.vovgoo.userservice.domain.redis.email.change.EmailChangeTokenKey;
import org.vovgoo.userservice.domain.redis.phone.change.PhoneChangeAttemptsKey;
import org.vovgoo.userservice.dto.user.request.ChangeEmailRequest;
import org.vovgoo.userservice.dto.user.request.ConfirmChangeEmailRequest;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.exception.custom.user.EmailAlreadyExistsException;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.ChangeEmailRequestNotFoundException;
import org.vovgoo.userservice.exception.custom.verification.InvalidOtpException;
import org.vovgoo.userservice.exception.custom.verification.OtpAttemptsExceededException;
import org.vovgoo.userservice.exception.custom.verification.OtpNotFoundException;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.service.rabbit.EventService;
import org.vovgoo.userservice.service.redis.RedisService;
import org.vovgoo.userservice.service.user.ChangeEmailService;
import org.vovgoo.userservice.utils.VerificationUtils;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class ChangeEmailServiceImpl implements ChangeEmailService {

    private final RedisService redisService;
    private final EventService eventService;
    private final UserRepository userRepository;
    private final VerificationProperty verificationProperty;

    @Override
    @CheckUserStatus
    public void changeEmail(ChangeEmailRequest request) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        userRepository.findByEmail(request.email())
                .ifPresent( u -> {throw new EmailAlreadyExistsException(request.email()); });

        String email = request.email();
        UUID token = VerificationUtils.generateEmailToken();

        redisService.set(EmailChangeRequestKey.of(userId), request);
        redisService.set(EmailChangeTokenKey.of(userId), token);
        redisService.set(PhoneChangeAttemptsKey.of(userId), 0);

        eventService.publishEmailChangeEvent(email, token);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void confirmChangeEmail(ConfirmChangeEmailRequest request) {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(UserNotFoundException::new);

        ChangeEmailRequest changeEmailRequest = redisService.get(EmailChangeRequestKey.of(userId))
                .orElseThrow(ChangeEmailRequestNotFoundException::new);

        UUID providedToken = request.token();

        UUID actualToken = redisService.get(EmailChangeTokenKey.of(userId))
                .orElseThrow(OtpNotFoundException::new);

        AtomicInteger attempts = new AtomicInteger(redisService.get(EmailChangeAttemptsKey.of(userId)).orElse(0));

        boolean valid = VerificationUtils.verifyToken(
                actualToken.toString(),
                providedToken.toString(),
                attempts,
                verificationProperty.getAttempts().getPhone()
        );

        redisService.set(EmailChangeAttemptsKey.of(userId), attempts.get());

        if (!valid) {
            redisService.delete(EmailChangeRequestKey.of(userId));
            redisService.delete(EmailChangeTokenKey.of(userId));
            redisService.delete(EmailChangeAttemptsKey.of(userId));

            if (attempts.get() >= verificationProperty.getAttempts().getPhone()) {
                throw new OtpAttemptsExceededException();
            }

            throw new InvalidOtpException();
        }

        redisService.delete(EmailChangeRequestKey.of(userId));
        redisService.delete(EmailChangeTokenKey.of(userId));
        redisService.delete(EmailChangeAttemptsKey.of(userId));

        userRepository.findByEmail(changeEmailRequest.email())
                .ifPresent(u -> { throw new EmailAlreadyExistsException(changeEmailRequest.email()); });

        user.setEmail(changeEmailRequest.email());
        userRepository.save(user);
    }
}
