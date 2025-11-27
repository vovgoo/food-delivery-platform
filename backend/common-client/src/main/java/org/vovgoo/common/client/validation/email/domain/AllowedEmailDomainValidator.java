package org.vovgoo.common.client.validation.email.domain;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.vovgoo.common.client.emailDomain.EmailDomainClientService;

@Component
@RequiredArgsConstructor
public class AllowedEmailDomainValidator implements ConstraintValidator<AllowedEmailDomain, String> {

    private final EmailDomainClientService emailDomainClientService;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }

        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        return emailDomainClientService.checkDomain(domain);
    }
}
