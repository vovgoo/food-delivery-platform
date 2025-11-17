package org.vovgoo.userservice.validators.email.domain;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vovgoo.userservice.repository.EmailDomainRepository;

@Component
@RequiredArgsConstructor
public class AllowedEmailDomainValidator implements ConstraintValidator<AllowedEmailDomain, String> {

    private final EmailDomainRepository emailDomainRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }

        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        return emailDomainRepository.findByDomainAndAllowedTrue(domain).isPresent();
    }
}
