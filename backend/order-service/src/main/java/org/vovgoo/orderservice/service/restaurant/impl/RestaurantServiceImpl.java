package org.vovgoo.orderservice.service.restaurant.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.common.client.restaurant.RestaurantClientService;
import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.common.domain.dish.enums.DishStatus;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;
import org.vovgoo.common.domain.restaurant.enums.RestaurantStatus;
import org.vovgoo.orderservice.exception.custom.dish.DishNotAvailableException;
import org.vovgoo.orderservice.exception.custom.restaurant.RestaurantInactiveException;
import org.vovgoo.orderservice.service.restaurant.RestaurantService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantClientService restaurantClient;

    @Override
    public RestaurantInternalResponse getRestaurant(UUID restaurantId) {
        return restaurantClient.getRestaurant(restaurantId);
    }

    @Override
    public void validateRestaurant(UUID restaurantId) {
        RestaurantInternalResponse restaurant = restaurantClient.getRestaurant(restaurantId);
        if (!RestaurantStatus.ACTIVE.equals(restaurant.status())) {
            throw new RestaurantInactiveException();
        }
    }

    @Override
    public List<DishInternalResponse> getDishesByRestaurant(UUID restaurantId, List<UUID> dishIds) {
        return restaurantClient.getDishesByRestaurant(restaurantId, dishIds);
    }

    @Override
    public List<DishInternalResponse> getAvailableDishes(UUID restaurantId, List<UUID> dishIds) {
        List<DishInternalResponse> dishes = restaurantClient.getDishesByRestaurant(restaurantId, dishIds);

        List<DishInternalResponse> unavailable = dishes.stream()
                .filter(d -> !DishStatus.AVAILABLE.equals(d.status()))
                .toList();

        if (!unavailable.isEmpty()) {
            throw new DishNotAvailableException();
        }

        return dishes;
    }
}
