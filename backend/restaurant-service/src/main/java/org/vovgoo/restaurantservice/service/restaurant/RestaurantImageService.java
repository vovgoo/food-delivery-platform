package org.vovgoo.restaurantservice.service.restaurant;

import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.entity.Restaurant;

import java.util.UUID;

public interface RestaurantImageService {
    void upload(Restaurant restaurant, MultipartFile file, boolean isProfile);
    void remove(Restaurant restaurant, UUID imageId, boolean isProfile);
}
