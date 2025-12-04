package org.vovgoo.restaurantservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vovgoo.restaurantservice.entity.Restaurant;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    @Query("SELECT r FROM Restaurant r WHERE r.status <> 'CLOSED' AND (:cuisine IS NULL OR LOWER(r.cuisine) LIKE LOWER(CONCAT('%', :cuisine, '%'))) ORDER BY r.name ASC")
    Page<Restaurant> findByCuisineContainingIgnoreCase(String cuisine, Pageable pageable);

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id AND r.status <> 'CLOSED'")
    Optional<Restaurant> findByIdAndStatusNotClosed(@Param("id") UUID id);

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id")
    Optional<Restaurant> findByIdIgnoreStatus(@Param("id") UUID id);
}
