package org.vovgoo.restaurantservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.restaurantservice.dto.common.PageParams;
import org.vovgoo.restaurantservice.dto.common.PageResponse;
import org.vovgoo.restaurantservice.dto.dish.request.DishCreateRequest;
import org.vovgoo.restaurantservice.dto.dish.request.DishUpdateRequest;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.restaurantservice.entity.Dish;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.restaurantservice.exception.custom.DishNotBelongsToRestaurantException;
import org.vovgoo.restaurantservice.exception.custom.DishNotFoundException;
import org.vovgoo.restaurantservice.exception.custom.RestaurantNotFoundException;
import org.vovgoo.restaurantservice.mapper.DishMapper;
import org.vovgoo.restaurantservice.repository.DishRepository;
import org.vovgoo.restaurantservice.repository.RestaurantRepository;
import org.vovgoo.restaurantservice.service.DishService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DishServiceImpl implements DishService {

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final DishMapper dishMapper;

    @Override
    public PageResponse<DishResponse> listByRestaurant(Long restaurantId, PageParams pageParams) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());
        Page<Dish> page = dishRepository.findByRestaurant(restaurant, pageRequest);

        return PageResponse.of(page.map(dishMapper::toResponse));
    }

    @Override
    @Transactional
    public DishResponse create(Long restaurantId, DishCreateRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        Dish dish = Dish.builder()
                .name(request.name())
                .description(request.description())
                .price(request.price())
                .imageUrl(request.imageUrl())
                .restaurant(restaurant)
                .build();

        dish = dishRepository.save(dish);

        return dishMapper.toResponse(dish);
    }

    @Override
    @Transactional
    public DishResponse update(Long restaurantId, Long dishId, DishUpdateRequest request) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(DishNotFoundException::new);

        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new DishNotBelongsToRestaurantException(dishId, restaurantId);
        }

        dish.setName(request.name());
        dish.setDescription(request.description());
        dish.setPrice(request.price());
        dish.setImageUrl(request.imageUrl());

        dish = dishRepository.save(dish);

        return dishMapper.toResponse(dish);
    }

    @Override
    @Transactional
    public void delete(Long restaurantId, Long dishId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(DishNotFoundException::new);

        if (!dish.getRestaurant().getId().equals(restaurantId)) {
            throw new DishNotBelongsToRestaurantException(dishId, restaurantId);
        }

        dishRepository.delete(dish);
    }
}
