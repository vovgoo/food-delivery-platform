package org.vovgoo.orderservice.service.restaurant.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vovgoo.dto.dish.DishShortResponse;
import org.vovgoo.dto.dish.enums.DishStatus;
import org.vovgoo.dto.restaurant.RestaurantShortResponse;
import org.vovgoo.dto.restaurant.enums.RestaurantStatus;
import org.vovgoo.orderservice.exception.custom.dish.DishNotAvailableException;
import org.vovgoo.orderservice.exception.custom.restaurant.RestaurantNotFoundException;
import org.vovgoo.orderservice.exception.custom.restaurant.RestaurantServiceException;
import org.vovgoo.orderservice.service.restaurant.InternalRestaurantClient;
import org.vovgoo.orderservice.service.restaurant.RestaurantService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final InternalRestaurantClient internalRestaurantClient;

    @Override
    public RestaurantShortResponse getRestaurant(UUID restaurantId) {
        try {
            return internalRestaurantClient.getRestaurant(restaurantId);
        } catch (FeignException.NotFound e) {
            throw new RestaurantNotFoundException();
        } catch (FeignException e) {
            throw new RestaurantServiceException();
        }
    }

    @Override
    public void validateRestaurant(UUID restaurantId) {
        RestaurantShortResponse restaurant = getRestaurant(restaurantId);

        if (!RestaurantStatus.ACTIVE.equals(restaurant.status())) {
            throw new RestaurantServiceException();
        }
    }

    @Override
    public List<DishShortResponse> getDishesByRestaurant(UUID restaurantId, List<UUID> dishIds) {
        try {
            return internalRestaurantClient.getDishesByRestaurant(restaurantId, dishIds);
        } catch (FeignException.NotFound e) {
            throw new DishNotAvailableException();
        } catch (FeignException e) {
            throw new RestaurantServiceException();
        }
    }

    @Override
    public List<DishShortResponse> getAvailableDishesByRestaurant(UUID restaurantId, List<UUID> dishIds) {
        List<DishShortResponse> dishes = getDishesByRestaurant(restaurantId, dishIds);

        List<DishShortResponse> unavailable = dishes.stream()
                .filter(d -> !DishStatus.AVAILABLE.equals(d.status()))
                .toList();

        if (!unavailable.isEmpty()) {
            throw new DishNotAvailableException();
        }

        return dishes;
    }
}
