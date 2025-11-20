package org.vovgoo.restaurantservice.service.dish.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.restaurantservice.dto.dish.request.DishCreateRequest;
import org.vovgoo.restaurantservice.dto.dish.request.DishUpdateRequest;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.restaurantservice.entity.Dish;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.restaurantservice.entity.enums.DishStatus;
import org.vovgoo.restaurantservice.exception.custom.dish.DishNotFoundException;
import org.vovgoo.restaurantservice.exception.custom.restaurant.RestaurantNotFoundException;
import org.vovgoo.restaurantservice.mapper.DishMapper;
import org.vovgoo.restaurantservice.repository.DishRepository;
import org.vovgoo.restaurantservice.repository.RestaurantRepository;
import org.vovgoo.restaurantservice.service.dish.DishImageService;
import org.vovgoo.restaurantservice.service.dish.DishService;
import org.vovgoo.user.aspect.CheckUserStatus;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DishServiceImpl implements DishService {

    private final DishImageService dishImageService;
    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishMapper dishMapper;

    @Override
    public PageResponse<DishResponse> listByRestaurant(UUID restaurantId, PageParams pageParams) {
        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());

        Page<DishResponse> page = dishRepository.findAllByRestaurantId(restaurantId, pageRequest)
                .map(dishMapper::toResponse);

        return PageResponse.of(page);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public DishResponse create(UUID restaurantId, DishCreateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        Dish dish = Dish.builder()
                .name(request.name())
                .description(request.description())
                .portionInGrams(request.portionInGrams())
                .proteins(request.proteins())
                .fats(request.fats())
                .carbohydrates(request.carbohydrates())
                .spicy(request.spicy())
                .vegan(request.vegan())
                .vegetarian(request.vegetarian())
                .price(request.price())
                .status(DishStatus.AVAILABLE)
                .restaurant(restaurant)
                .build();

        dish = dishRepository.save(dish);

        return dishMapper.toResponse(dish);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public DishResponse update(UUID restaurantId, UUID dishId, DishUpdateRequest request) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdWithImages(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);

        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setPortionInGrams(request.portionInGrams());
        dish.setProteins(request.proteins());
        dish.setFats(request.fats());
        dish.setCarbohydrates(request.carbohydrates());
        dish.setSpicy(request.spicy());
        dish.setVegan(request.vegan());
        dish.setVegetarian(request.vegetarian());
        dish.setPrice(request.price());
        dish.setStatus(request.status());

        dish = dishRepository.save(dish);

        return dishMapper.toResponse(dish);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void delete(UUID restaurantId, UUID dishId) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);

        dish.setStatus(DishStatus.REMOVED);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void uploadImage(UUID restaurantId, UUID dishId, MultipartFile file) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);
        dishImageService.upload(dish, file, false);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void deleteImage(UUID restaurantId, UUID dishId, UUID imageId) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);
        dishImageService.remove(dish, imageId, false);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void setProfileImage(UUID restaurantId, UUID dishId, MultipartFile file) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);
        dishImageService.upload(dish, file, true);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void removeProfileImage(UUID restaurantId, UUID dishId) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);
        dishImageService.remove(dish, null, true);
    }
}
