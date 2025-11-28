package org.vovgoo.restaurantservice.service.image.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.common.domain.image.enums.ImageType;
import org.vovgoo.restaurantservice.config.image.ImageLimitProperties;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.restaurantservice.service.image.handler.ImageHandler;
import org.vovgoo.restaurantservice.service.image.service.ImageService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantImageHandler implements ImageHandler<Restaurant> {

    private final ImageService imageService;
    private final ImageLimitProperties imageLimitProperties;

    @Override
    public void uploadImage(Restaurant entity, MultipartFile file) {
        imageService.uploadImage(entity.getId(), ImageType.RESTAURANT, file, imageLimitProperties.getRestaurant());
    }

    @Override
    public void removeImage(Restaurant entity, UUID imageId) {
        imageService.removeImage(entity.getId(), imageId, ImageType.RESTAURANT);
    }

    @Override
    public void uploadProfileImage(Restaurant entity, MultipartFile file) {
        imageService.uploadProfileImage(entity.getId(), ImageType.RESTAURANT, file);
    }

    @Override
    public void removeProfileImage(Restaurant entity) {
        imageService.removeProfileImage(entity.getId(), ImageType.RESTAURANT);
    }

    @Override
    public Class<Restaurant> getEntityClass() {
        return Restaurant.class;
    }
}
