package org.vovgoo.common.domain.validation.restaurant.status;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.vovgoo.common.domain.restaurant.enums.RestaurantStatus;

import java.util.Arrays;

public class AllowedRestaurantStatusValidator implements ConstraintValidator<AllowedRestaurantStatus, RestaurantStatus> {

    private RestaurantStatus[] allowed;

    @Override
    public void initialize(AllowedRestaurantStatus constraintAnnotation) {
        this.allowed = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(RestaurantStatus value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return Arrays.asList(allowed).contains(value);
    }
}
