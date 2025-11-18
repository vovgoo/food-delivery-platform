package org.vovgoo.userservice.service.user.aspects.checkstatus;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.enums.UserStatus;
import org.vovgoo.userservice.exception.custom.user.*;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.utils.CurrentUserUtils;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckUserStatusAspect {

    private final UserRepository userRepository;

    @Around("@annotation(check)")
    public Object checkStatus(ProceedingJoinPoint joinPoint, CheckUserStatus check) throws Throwable {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        UserStatus status = user.getStatus();

        for (UserStatus forbidden : check.forbidden()) {
            if (status == forbidden) {
                throw exceptionForStatus(status);
            }
        }

        return joinPoint.proceed();
    }

    private RuntimeException exceptionForStatus(UserStatus status) {
        return switch (status) {
            case BLOCKED -> new UserBlockedException();
            case DEACTIVATED -> new UserDeactivatedException();
            case ACTIVE -> new UserActiveException();
            default -> new UnsupportedUserStatusException(status);
        };
    }
}
