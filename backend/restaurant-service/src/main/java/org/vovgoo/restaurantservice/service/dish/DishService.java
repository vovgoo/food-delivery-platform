package org.vovgoo.restaurantservice.service.dish;

import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.common.domain.dto.pageable.PageParams;
import org.vovgoo.common.domain.dto.pageable.PageResponse;
import org.vovgoo.restaurantservice.dto.dish.request.DishCreateRequest;
import org.vovgoo.restaurantservice.dto.dish.request.DishUpdateRequest;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.restaurantservice.dto.dish.response.DishShortResponse;

import java.util.List;
import java.util.UUID;

public interface DishService {
    PageResponse<DishShortResponse> listByRestaurant(UUID restaurantId, PageParams pageParams);
    DishResponse getById(UUID restaurantId, UUID dishId);
    DishResponse create(UUID restaurantId, DishCreateRequest request);
    DishResponse update(UUID restaurantId, UUID dishId, DishUpdateRequest request);
    void delete(UUID restaurantId, UUID dishId);
    void uploadImage(UUID restaurantId, UUID dishId, MultipartFile file);
    void deleteImage(UUID restaurantId, UUID dishId, UUID imageId);
    void setProfileImage(UUID restaurantId, UUID dishId, MultipartFile file);
    void removeProfileImage(UUID restaurantId, UUID dishId);
    List<DishInternalResponse> getInternalDishesByRestaurant(UUID restaurantId, List<UUID> dishIds);
}
