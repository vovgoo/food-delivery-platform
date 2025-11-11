package org.vovgoo.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.vovgoo.userservice.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u left join fetch u.roles r where u.email = :email")
    Optional<User> findByEmailWithRoles(String email);

    @Query("select u from User u left join fetch u.roles left join fetch u.addresses where u.id = :id")
    Optional<User> findByIdWithRolesAndAddresses(Long id);

    boolean existsByEmail(String email);
}
