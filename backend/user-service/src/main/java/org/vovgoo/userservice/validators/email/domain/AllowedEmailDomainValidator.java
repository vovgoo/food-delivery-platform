package org.vovgoo.userservice.validators.email.domain;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vovgoo.userservice.repository.EmailDomainRepository;

@Component
public class AllowedEmailDomainValidator implements ConstraintValidator<AllowedEmailDomain, String> {

    private EmailDomainRepository emailDomainRepository;

    @Autowired
    public void setEmailDomainRepository(EmailDomainRepository emailDomainRepository) {
        this.emailDomainRepository = emailDomainRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }

        String domain = email.substring(email.indexOf("@") + 1).toLowerCase();
        return emailDomainRepository.findByDomainAndAllowedTrue(domain).isPresent();
    }
}
