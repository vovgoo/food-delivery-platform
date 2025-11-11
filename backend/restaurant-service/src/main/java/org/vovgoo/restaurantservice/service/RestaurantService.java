package org.vovgoo.restaurantservice.service;

import org.vovgoo.restaurantservice.dto.common.PageParams;
import org.vovgoo.restaurantservice.dto.common.PageResponse;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantCreateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantSearchRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantUpdateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;

public interface RestaurantService {
    PageResponse<RestaurantResponse> listRestaurants(RestaurantSearchRequest restaurantSearchRequest, PageParams pageParams);
    RestaurantResponse getById(Long id);
    RestaurantResponse create(RestaurantCreateRequest restaurantCreateRequest);
    RestaurantResponse update(Long id, RestaurantUpdateRequest restaurantUpdateRequest);
    void delete(Long id);
}
