package org.vovgoo.userservice.service.user.aspects.checkstatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckUserStatus {

}
