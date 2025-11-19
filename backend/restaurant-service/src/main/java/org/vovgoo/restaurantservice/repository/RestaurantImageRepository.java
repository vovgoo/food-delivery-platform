package org.vovgoo.restaurantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.restaurantservice.entity.RestaurantImage;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, UUID> {
    Optional<RestaurantImage> findByRestaurantIdAndId(UUID restaurantId, UUID imageId);
}
