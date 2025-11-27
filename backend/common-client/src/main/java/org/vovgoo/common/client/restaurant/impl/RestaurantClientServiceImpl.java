package org.vovgoo.common.client.restaurant.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.vovgoo.common.client.exception.custom.FeignServiceException;
import org.vovgoo.common.client.exception.custom.NotFoundException;
import org.vovgoo.common.client.restaurant.InternalRestaurantClient;
import org.vovgoo.common.client.restaurant.RestaurantClientService;
import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantClientServiceImpl implements RestaurantClientService {

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
            throw new NotFoundException("Адрес не найден", e);
        } catch (FeignException e) {
            throw new FeignServiceException("Ошибка получения данных о ресторане", e);
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
            throw new NotFoundException("Адрес не найден", e);
        } catch (FeignException e) {
            throw new FeignServiceException("Ошибка получения данных о блюдах", e);
        }
    }
}
