package org.vovgoo.restaurantservice.service.image.handler.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.common.domain.image.enums.ImageType;
import org.vovgoo.restaurantservice.config.image.ImageLimitProperties;
import org.vovgoo.restaurantservice.entity.Dish;
import org.vovgoo.restaurantservice.service.image.handler.ImageHandler;
import org.vovgoo.restaurantservice.service.image.service.ImageService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishImageHandler implements ImageHandler<Dish> {

    private final ImageService imageService;
    private final ImageLimitProperties imageLimitProperties;

    @Override
    public void uploadImage(Dish entity, MultipartFile file) {
        imageService.uploadImage(entity.getId(), ImageType.DISH, file, imageLimitProperties.getDish());
    }

    @Override
    public void removeImage(Dish entity, UUID imageId) {
        imageService.removeImage(entity.getId(), imageId, ImageType.DISH);
    }

    @Override
    public void uploadProfileImage(Dish entity, MultipartFile file) {
        imageService.uploadProfileImage(entity.getId(), ImageType.DISH, file);
    }

    @Override
    public void removeProfileImage(Dish entity) {
        imageService.removeProfileImage(entity.getId(), ImageType.DISH);
    }

    @Override
    public Class<Dish> getEntityClass() {
        return Dish.class;
    }
}
