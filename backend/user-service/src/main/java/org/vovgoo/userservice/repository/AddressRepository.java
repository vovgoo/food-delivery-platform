package org.vovgoo.userservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vovgoo.userservice.entity.Address;

import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    @Query("SELECT a FROM Address a WHERE a.id = :id AND a.user.id = :userId AND a.addressStatus = 'ACTIVE'")
    Optional<Address> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.isDefault = true AND a.addressStatus = 'ACTIVE'")
    Optional<Address> findDefaultByUserId(UUID userId);

    @Query("SELECT a FROM Address a WHERE a.user.id = :userId AND a.addressStatus = 'ACTIVE'")
    Page<Address> findAllByUserIdAndAddressStatus(Pageable pageable, UUID usedId);
}
