package org.vovgoo.restaurantservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.vovgoo.restaurantservice.entity.Dish;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DishRepository extends JpaRepository<Dish, UUID> {

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId AND d.status <> 'REMOVED' ORDER BY d.name ASC")
    Page<Dish> findAllByRestaurantId(@Param("restaurantId") UUID restaurantId, Pageable pageable);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId AND d.id = :dishId AND d.status <> 'REMOVED'")
    Optional<Dish> findByRestaurantIdAndDishId(@Param("restaurantId") UUID restaurantId, @Param("dishId") UUID dishId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId AND d.id IN :dishIds AND d.status <> 'REMOVED'")
    List<Dish> findAllByRestaurantIdAndDishIds(@Param("restaurantId") UUID restaurantId, @Param("dishIds") List<UUID> dishIds);
}
