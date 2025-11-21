package org.vovgoo.restaurantservice.service.dish;

import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.dto.dish.DishInternalResponse;
import org.vovgoo.restaurantservice.dto.dish.request.DishCreateRequest;
import org.vovgoo.restaurantservice.dto.dish.request.DishUpdateRequest;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;

import java.util.List;
import java.util.UUID;

public interface DishService {
    PageResponse<DishResponse> listByRestaurant(UUID restaurantId, PageParams pageParams);
    DishResponse create(UUID restaurantId, DishCreateRequest request);
    DishResponse update(UUID restaurantId, UUID dishId, DishUpdateRequest request);
    void delete(UUID restaurantId, UUID dishId);
    void uploadImage(UUID restaurantId, UUID dishId, MultipartFile file);
    void deleteImage(UUID restaurantId, UUID dishId, UUID imageId);
    void setProfileImage(UUID restaurantId, UUID dishId, MultipartFile file);
    void removeProfileImage(UUID restaurantId, UUID dishId);
    List<DishInternalResponse> getInternalDishesByRestaurant(UUID restaurantId, List<UUID> dishIds);
}
