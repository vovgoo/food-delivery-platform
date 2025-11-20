package org.vovgoo.restaurantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.restaurantservice.entity.DishImage;

import java.util.Optional;
import java.util.UUID;

public interface DishImageRepository extends JpaRepository<DishImage, UUID> {
    Optional<DishImage> findByDishIdAndId(UUID restaurantId, UUID imageId);
}
