package org.vovgoo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.userservice.entity.Role;
import org.vovgoo.enums.user.RoleType;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleType roleType);
}
