package org.vovgoo.common.domain.validation.age.min;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinAgeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface MinAge {
    int value();
    String message() default "Возраст должен быть не меньше {value} лет";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
