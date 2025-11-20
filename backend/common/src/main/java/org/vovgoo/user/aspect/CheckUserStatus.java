package org.vovgoo.user.aspect;

import org.vovgoo.user.enums.UserStatus;
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
