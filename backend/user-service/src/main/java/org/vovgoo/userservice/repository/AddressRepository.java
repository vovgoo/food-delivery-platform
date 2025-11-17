package org.vovgoo.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vovgoo.userservice.entity.Address;
import org.vovgoo.userservice.entity.enums.AddressStatus;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Optional<Address> findByUserIdAndIsDefault(UUID id, boolean isDefault);
    Page<Address> findAllByUserIdAndAddressStatus(Pageable pageable, UUID usedId, AddressStatus addressStatus);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = :userId")
    void resetDefaultForUser(@Param("userId") UUID userId);
}
