package org.vovgoo.restaurantservice.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vovgoo.restaurantservice.dto.common.PageParams;
import org.vovgoo.restaurantservice.dto.common.PageResponse;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantCreateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantSearchRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantUpdateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.restaurantservice.mapper.RestaurantMapper;
import org.vovgoo.restaurantservice.repository.RestaurantRepository;
import org.vovgoo.restaurantservice.service.RestaurantService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public PageResponse<RestaurantResponse> listRestaurants(RestaurantSearchRequest searchRequest, PageParams pageParams) {
        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());
        Page<Restaurant> page;

        if (searchRequest.cuisine() != null && !searchRequest.cuisine().isEmpty()) {
            page = restaurantRepository.findByCuisineContainingIgnoreCase(searchRequest.cuisine(), pageRequest);
        } else {
            page = restaurantRepository.findAll(pageRequest);
        }

        return PageResponse.of(page.map(restaurantMapper::toResponse));
    }

    @Override
    public RestaurantResponse getById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ресторан не найден"));

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    public RestaurantResponse create(RestaurantCreateRequest restaurantCreateRequest) {
        Restaurant restaurant = Restaurant.builder()
                .name(restaurantCreateRequest.name())
                .cuisine(restaurantCreateRequest.cuisine())
                .address(restaurantCreateRequest.address())
                .build();

        restaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    public RestaurantResponse update(Long id, RestaurantUpdateRequest restaurantUpdateRequest) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ресторан не найден"));

        restaurant.setName(restaurantUpdateRequest.name());
        restaurant.setCuisine(restaurantUpdateRequest.cuisine());
        restaurant.setAddress(restaurantUpdateRequest.address());

        restaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new EntityNotFoundException("Ресторан не найден");
        }
        restaurantRepository.deleteById(id);
    }
}
