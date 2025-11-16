package org.vovgoo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.userservice.entity.EmailDomain;

import java.util.Optional;

public interface EmailDomainRepository extends JpaRepository<EmailDomain, Long> {
    Optional<EmailDomain> findByDomainAndAllowedTrue(String domain);
}
