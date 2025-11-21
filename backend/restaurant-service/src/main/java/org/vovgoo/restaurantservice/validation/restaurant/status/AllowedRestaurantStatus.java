package org.vovgoo.restaurantservice.validation.restaurant.status;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.vovgoo.dto.restaurant.enums.RestaurantStatus;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AllowedRestaurantStatusValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface AllowedRestaurantStatus {
    String message() default "Недопустимый статус ресторана";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    RestaurantStatus[] anyOf();
}
