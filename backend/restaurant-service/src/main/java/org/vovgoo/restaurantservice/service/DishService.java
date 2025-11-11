package org.vovgoo.restaurantservice.service;

import org.vovgoo.restaurantservice.dto.common.PageParams;
import org.vovgoo.restaurantservice.dto.common.PageResponse;
import org.vovgoo.restaurantservice.dto.dish.request.DishCreateRequest;
import org.vovgoo.restaurantservice.dto.dish.request.DishUpdateRequest;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;

public interface DishService {
    PageResponse<DishResponse> listByRestaurant(Long restaurantId, PageParams pageParams);
    DishResponse create(Long restaurantId, DishCreateRequest dishCreateRequest);
    DishResponse update(Long restaurantId, Long dishId, DishUpdateRequest dishUpdateRequest);
    void delete(Long restaurantId, Long dishId);
}
