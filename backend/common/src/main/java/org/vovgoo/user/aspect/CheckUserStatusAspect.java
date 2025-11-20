package org.vovgoo.user.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.vovgoo.security.utils.CurrentUserUtils;
import org.vovgoo.user.client.InternalUserClient;
import org.vovgoo.user.enums.UserStatus;
import org.vovgoo.user.exception.UserActiveException;
import org.vovgoo.user.exception.UserBlockedException;
import org.vovgoo.user.exception.UserDeactivatedException;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckUserStatusAspect {

    private final InternalUserClient internalUserClient;

    @Around("@annotation(check)")
    public Object checkStatus(ProceedingJoinPoint joinPoint, CheckUserStatus check) throws Throwable {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        UserStatus status = internalUserClient.getUserStatus(userId);

        for (UserStatus forbidden : check.forbidden()) {
            if (status == forbidden) {
                throw switch (status) {
                    case BLOCKED -> new UserBlockedException();
                    case DEACTIVATED -> new UserDeactivatedException();
                    case ACTIVE -> new UserActiveException();
                };
            }
        }

        return joinPoint.proceed();
    }
}
