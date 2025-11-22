package org.vovgoo.restaurantservice.validation.dish.status;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.vovgoo.enums.dish.DishStatus;

import java.util.Arrays;

public class AllowedDishStatusValidator implements ConstraintValidator<AllowedDishStatus, DishStatus> {

    private DishStatus[] allowed;

    @Override
    public void initialize(AllowedDishStatus constraintAnnotation) {
        this.allowed = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(DishStatus value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return Arrays.asList(allowed).contains(value);
    }
}