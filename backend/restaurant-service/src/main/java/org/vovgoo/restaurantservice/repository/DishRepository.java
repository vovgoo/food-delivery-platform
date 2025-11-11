package org.vovgoo.restaurantservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.vovgoo.restaurantservice.entity.Dish;
import org.vovgoo.restaurantservice.entity.Restaurant;

public interface DishRepository extends JpaRepository<Dish, Long> {

    Page<Dish> findByRestaurant(Restaurant restaurant, Pageable pageable);
}
