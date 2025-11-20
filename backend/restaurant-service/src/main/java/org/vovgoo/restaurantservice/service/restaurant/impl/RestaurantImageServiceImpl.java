package org.vovgoo.restaurantservice.service.restaurant.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.restaurantservice.entity.RestaurantImage;
import org.vovgoo.restaurantservice.exception.custom.restaurant.RestaurantImageNotFoundException;
import org.vovgoo.restaurantservice.repository.RestaurantImageRepository;
import org.vovgoo.restaurantservice.service.image.ImageService;
import org.vovgoo.restaurantservice.service.restaurant.RestaurantImageService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RestaurantImageServiceImpl implements RestaurantImageService {

    private final RestaurantImageRepository restaurantImageRepository;
    private final ImageService imageService;

    @Override
    @Transactional
    public void upload(Restaurant restaurant, MultipartFile file, boolean isProfile) {
        String imageUrl = imageService.uploadImage(file);

        if (isProfile && restaurant.getProfileImage() != null) {
            restaurantImageRepository.delete(restaurant.getProfileImage());
        }

        RestaurantImage image = RestaurantImage.builder()
                .url(imageUrl)
                .isProfile(isProfile)
                .restaurant(restaurant)
                .build();

        image = restaurantImageRepository.save(image);

        if (isProfile) {
            restaurant.setProfileImage(image);
        }
    }

    @Override
    @Transactional
    public void remove(Restaurant restaurant, UUID imageId, boolean isProfile) {
        if (isProfile) {
            RestaurantImage profileImage = restaurant.getProfileImage();
            if (profileImage == null) {
                throw new RestaurantImageNotFoundException();
            }
            restaurant.setProfileImage(null);
            restaurantImageRepository.delete(profileImage);
        } else {
            RestaurantImage image = restaurantImageRepository.findByRestaurantIdAndId(restaurant.getId(), imageId)
                    .orElseThrow(RestaurantImageNotFoundException::new);
            restaurantImageRepository.delete(image);
        }
    }
}
