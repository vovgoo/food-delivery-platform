package org.vovgoo.restaurantservice.service.restaurant;

import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.restaurantservice.entity.RestaurantImage;

import java.util.UUID;

public interface RestaurantImageService {
    RestaurantImage upload(Restaurant restaurant, MultipartFile file, boolean isProfile);
    void remove(Restaurant restaurant, UUID imageId, boolean isProfile);
}
