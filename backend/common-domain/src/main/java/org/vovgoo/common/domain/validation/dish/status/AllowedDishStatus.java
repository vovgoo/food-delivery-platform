package org.vovgoo.common.domain.validation.dish.status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.vovgoo.common.domain.dish.enums.DishStatus;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedDishStatusValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedDishStatus {
    String message() default "Недопустимый статус блюда";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    DishStatus[] anyOf();
}
