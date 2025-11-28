package org.vovgoo.userservice.service.emailDomain.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.userservice.repository.EmailDomainRepository;
import org.vovgoo.userservice.service.emailDomain.EmailDomainService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailDomainServiceImpl implements EmailDomainService {

    private final EmailDomainRepository emailDomainRepository;

    @Override
    public boolean check(String domain) {
        return emailDomainRepository.checkDomain(domain);
    }
}
