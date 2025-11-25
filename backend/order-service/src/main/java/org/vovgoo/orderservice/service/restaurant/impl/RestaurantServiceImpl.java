package org.vovgoo.orderservice.service.restaurant.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.dto.dish.DishInternalResponse;
import org.vovgoo.enums.dish.DishStatus;
import org.vovgoo.dto.restaurant.RestaurantInternalResponse;
import org.vovgoo.enums.restaurant.RestaurantStatus;
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
    @Retryable(
            retryFor = { FeignException.class },
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public RestaurantInternalResponse getRestaurant(UUID restaurantId) {
        try {
            return internalRestaurantClient.getRestaurant(restaurantId);
        } catch (FeignException.NotFound e) {
            throw new RestaurantNotFoundException();
        } catch (FeignException e) {
            throw new RestaurantServiceException();
        }
    }

    @Override
    @Retryable(
            retryFor = { FeignException.class },
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void validateRestaurant(UUID restaurantId) {
        RestaurantInternalResponse restaurant = getRestaurant(restaurantId);

        if (!RestaurantStatus.ACTIVE.equals(restaurant.status())) {
            throw new RestaurantServiceException();
        }
    }

    @Override
    @Retryable(
            retryFor = { FeignException.class },
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public List<DishInternalResponse> getDishesByRestaurant(UUID restaurantId, List<UUID> dishIds) {
        try {
            return internalRestaurantClient.getDishesByRestaurant(restaurantId, dishIds);
        } catch (FeignException.NotFound e) {
            throw new DishNotAvailableException();
        } catch (FeignException e) {
            throw new RestaurantServiceException();
        }
    }

    @Override
    public List<DishInternalResponse> getAvailableDishesByRestaurant(UUID restaurantId, List<UUID> dishIds) {
        List<DishInternalResponse> dishes = getDishesByRestaurant(restaurantId, dishIds);

        List<DishInternalResponse> unavailable = dishes.stream()
                .filter(d -> !DishStatus.AVAILABLE.equals(d.status()))
                .toList();

        if (!unavailable.isEmpty()) {
            throw new DishNotAvailableException();
        }

        return dishes;
    }
}
