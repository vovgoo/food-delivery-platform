package org.vovgoo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.userservice.entity.Address;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByUserIdAndIsDefault(UUID id, boolean isDefault);
}
