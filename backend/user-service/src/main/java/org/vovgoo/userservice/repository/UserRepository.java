package org.vovgoo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vovgoo.userservice.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    @Query("select u from User u left join fetch u.roles r where u.phone = :phone")
    Optional<User> findByPhoneWithRoles(@Param("phone") String phone);

    @Query("select u from User u left join fetch u.roles r where u.id = :id")
    Optional<User> findByIdWithRoles(@Param("id") UUID id);
}
