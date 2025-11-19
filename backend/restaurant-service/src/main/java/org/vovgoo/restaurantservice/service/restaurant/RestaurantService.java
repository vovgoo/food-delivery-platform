package org.vovgoo.restaurantservice.service.restaurant;

import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantSearchRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantCreateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantUpdateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;

import java.util.UUID;

public interface RestaurantService {
    PageResponse<RestaurantResponse> listRestaurants(RestaurantSearchRequest searchRequest, PageParams pageParams);
    RestaurantResponse getById(UUID restaurantId);
    RestaurantResponse create(RestaurantCreateRequest request);
    RestaurantResponse update(UUID restaurantId, RestaurantUpdateRequest request);
    void delete(UUID restaurantId);
    void uploadImage(UUID restaurantId, MultipartFile file);
    void deleteImage(UUID restaurantId, UUID imageId);
    void setProfileImage(UUID restaurantId, MultipartFile file);
    void removeProfileImage(UUID restaurantId);
}
