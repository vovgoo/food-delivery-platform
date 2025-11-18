package org.vovgoo.userservice.validators.email.domain;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedEmailDomainValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedEmailDomain {
    String message() default "Почта содержит не поддерживаемый домен";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
