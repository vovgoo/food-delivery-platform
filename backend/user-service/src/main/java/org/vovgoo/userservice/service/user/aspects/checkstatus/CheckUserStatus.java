package org.vovgoo.userservice.service.user.aspects.checkstatus;

import org.vovgoo.userservice.entity.enums.UserStatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckUserStatus {

    UserStatus[] forbidden() default {
            UserStatus.BLOCKED,
            UserStatus.DEACTIVATED
    };
}
