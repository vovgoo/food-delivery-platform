package org.vovgoo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.userservice.entity.EmailDomain;

import java.util.Optional;
import java.util.UUID;

public interface EmailDomainRepository extends JpaRepository<EmailDomain, UUID> {
    Optional<EmailDomain> findByDomainAndAllowedTrue(String domain);
}
