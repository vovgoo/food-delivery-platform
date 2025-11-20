package org.vovgoo.restaurantservice.validation.dish.status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.vovgoo.restaurantservice.entity.enums.DishStatus;

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
