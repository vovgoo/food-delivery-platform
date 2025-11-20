package org.vovgoo.restaurantservice.service.dish;

import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.entity.Dish;

import java.util.UUID;

public interface DishImageService {
    void upload(Dish dish, MultipartFile file, boolean isProfile);
    void remove(Dish dish, UUID imageId, boolean isProfile);
}
