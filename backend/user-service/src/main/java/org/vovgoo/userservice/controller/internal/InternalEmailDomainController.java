package org.vovgoo.userservice.controller.internal;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vovgoo.userservice.service.emailDomain.EmailDomainService;

@RestController
@RequestMapping("/internal/email/domain")
@RequiredArgsConstructor
@Hidden
public class InternalEmailDomainController {

    private final EmailDomainService emailDomainService;

    @GetMapping("/check/{domain}")
    public ResponseEntity<Boolean> checkDomain(@PathVariable("domain") String domain) {
        return ResponseEntity.ok(emailDomainService.check(domain));
    }
}
