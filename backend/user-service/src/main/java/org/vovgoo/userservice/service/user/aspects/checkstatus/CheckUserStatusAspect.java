package org.vovgoo.userservice.service.user.aspects.checkstatus;


import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.vovgoo.userservice.entity.User;
import org.vovgoo.userservice.entity.enums.UserStatus;
import org.vovgoo.userservice.exception.custom.user.UserBlockedException;
import org.vovgoo.userservice.exception.custom.user.UserDeactivatedException;
import org.vovgoo.userservice.exception.custom.user.UserNotFoundException;
import org.vovgoo.userservice.repository.UserRepository;
import org.vovgoo.userservice.utils.CurrentUserUtils;

import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class CheckUserStatusAspect {

    private final UserRepository userRepository;

    @Around("@annotation(org.vovgoo.userservice.service.user.aspects.checkstatus.CheckUserStatus)")
    public Object checkStatus(ProceedingJoinPoint joinPoint) throws Throwable {
        UUID userId = CurrentUserUtils.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new UserBlockedException();
        }

        if (user.getStatus() == UserStatus.DEACTIVATED) {
            throw new UserDeactivatedException();
        }

        return joinPoint.proceed();
    }
}
