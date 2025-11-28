package org.vovgoo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vovgoo.userservice.entity.EmailDomain;

import java.util.UUID;

public interface EmailDomainRepository extends JpaRepository<EmailDomain, UUID> {
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EmailDomain e WHERE e.domain = :domain AND e.allowed = true")
    boolean checkDomain(@Param("domain") String domain);
}
