package org.vovgoo.restaurantservice.service.dish.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.common.domain.dish.enums.DishStatus;
import org.vovgoo.common.domain.dto.pageable.PageParams;
import org.vovgoo.common.domain.dto.pageable.PageResponse;
import org.vovgoo.common.domain.image.enums.ImageType;
import org.vovgoo.restaurantservice.dto.dish.request.DishCreateRequest;
import org.vovgoo.restaurantservice.dto.dish.request.DishUpdateRequest;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.restaurantservice.entity.Dish;
import org.vovgoo.restaurantservice.entity.Image;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.restaurantservice.exception.custom.dish.DishNotFoundException;
import org.vovgoo.restaurantservice.exception.custom.restaurant.RestaurantNotFoundException;
import org.vovgoo.restaurantservice.mapper.DishMapper;
import org.vovgoo.restaurantservice.repository.DishRepository;
import org.vovgoo.restaurantservice.repository.ImageRepository;
import org.vovgoo.restaurantservice.repository.RestaurantRepository;
import org.vovgoo.restaurantservice.service.dish.DishService;
import org.vovgoo.restaurantservice.service.image.facade.ImageFacadeService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final ImageRepository imageRepository;
    private final ImageFacadeService imageFacadeService;
    private final DishMapper dishMapper;

    @Override
    public PageResponse<DishResponse> listByRestaurant(UUID restaurantId, PageParams pageParams) {
        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());
        Page<Dish> dishesPage = dishRepository.findAllByRestaurantId(restaurantId, pageRequest);

        List<UUID> dishIds = dishesPage.map(Dish::getId).toList();

        List<Image> images = imageRepository.findAllByParentIdsAndType(dishIds, ImageType.DISH);

        Map<UUID, List<Image>> imagesMap = images.stream()
                .collect(Collectors.groupingBy(Image::getParentId));

        Page<DishResponse> dishResponses = dishesPage
                .map(d -> dishMapper.toResponse(d, imagesMap.getOrDefault(d.getId(), Collections.emptyList())));

        return PageResponse.of(dishResponses);
    }

    @Override
    @Transactional
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

        return dishMapper.toResponse(dish, List.of());
    }

    @Override
    @Transactional
    public DishResponse update(UUID restaurantId, UUID dishId, DishUpdateRequest request) {
        Dish dish = dishRepository.findByRestaurantIdAndDishId(restaurantId, dishId)
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

        List<Image> images = imageRepository.findAllByParentIdAndType(dishId, ImageType.DISH);

        return dishMapper.toResponse(dish, images);
    }

    @Override
    @Transactional
    public void delete(UUID restaurantId, UUID dishId) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);

        dish.setStatus(DishStatus.REMOVED);
    }

    @Override
    @Transactional
    public void uploadImage(UUID restaurantId, UUID dishId, MultipartFile file) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);

        imageFacadeService.uploadImage(dish, file);
    }

    @Override
    @Transactional
    public void deleteImage(UUID restaurantId, UUID dishId, UUID imageId) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);

        imageFacadeService.removeImage(dish, imageId);
    }

    @Override
    @Transactional
    public void setProfileImage(UUID restaurantId, UUID dishId, MultipartFile file) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);

        imageFacadeService.uploadProfileImage(dish, file);
    }

    @Override
    @Transactional
    public void removeProfileImage(UUID restaurantId, UUID dishId) {
        Dish dish = dishRepository.findByRestaurantIdAndDishIdAndStatusNotRemoved(restaurantId, dishId)
                .orElseThrow(DishNotFoundException::new);

        imageFacadeService.removeProfileImage(dish);
    }

    @Override
    public List<DishInternalResponse> getInternalDishesByRestaurant(UUID restaurantId, List<UUID> dishIds) {
        List<Dish> dishes = dishRepository.findAllByRestaurantIdAndDishIds(restaurantId, dishIds);

        if (dishes.size() != dishIds.size()) {
            throw new DishNotFoundException();
        }

        List<Image> profileImages = imageRepository.findProfileImagesByParentIds(dishIds, ImageType.DISH);

        Map<UUID, Image> profileImagesMap = profileImages.stream()
                .collect(Collectors.toMap(Image::getParentId, Function.identity()));

        return dishes.stream()
                .map(d -> dishMapper.toShortResponse(d, profileImagesMap.get(d.getId())))
                .toList();
    }
}
