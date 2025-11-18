package org.vovgoo.userservice.validators.phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isEmpty()) {
            return true;
        }
        return PhoneValidationUtil.isValid(phone);
    }
}
