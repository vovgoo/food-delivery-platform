package org.vovgoo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.userservice.entity.enums.RoleType;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType roleType);
}
