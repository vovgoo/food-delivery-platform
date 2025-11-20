package org.vovgoo.restaurantservice.service.dish.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.entity.Dish;
import org.vovgoo.restaurantservice.entity.DishImage;
import org.vovgoo.restaurantservice.exception.custom.dish.DishImageNotFoundException;
import org.vovgoo.restaurantservice.exception.custom.restaurant.RestaurantImageNotFoundException;
import org.vovgoo.restaurantservice.repository.DishImageRepository;
import org.vovgoo.restaurantservice.service.dish.DishImageService;
import org.vovgoo.restaurantservice.service.image.ImageService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishImageServiceImpl implements DishImageService {

    private final DishImageRepository dishImageRepository;
    private final ImageService imageService;

    @Override
    @Transactional
    public void upload(Dish dish, MultipartFile file, boolean isProfile) {
        String imageUrl = imageService.uploadImage(file);

        if (isProfile && dish.getProfileImage() != null) {
            dishImageRepository.delete(dish.getProfileImage());
        }

        DishImage image = DishImage.builder()
                .url(imageUrl)
                .isProfile(isProfile)
                .dish(dish)
                .build();

        image = dishImageRepository.save(image);

        if (isProfile) {
            dish.setProfileImage(image);
        }
    }

    @Override
    @Transactional
    public void remove(Dish dish, UUID imageId, boolean isProfile) {
        if (isProfile) {
            DishImage profileImage = dish.getProfileImage();
            if (profileImage == null) {
                throw new RestaurantImageNotFoundException();
            }
            dish.setProfileImage(null);
            dishImageRepository.delete(profileImage);
        } else {
            DishImage image = dishImageRepository.findByDishIdAndId(dish.getId(), imageId)
                    .orElseThrow(DishImageNotFoundException::new);
            dishImageRepository.delete(image);
        }
    }
}