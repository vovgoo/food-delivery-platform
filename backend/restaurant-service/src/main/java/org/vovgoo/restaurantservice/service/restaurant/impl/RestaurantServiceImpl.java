package org.vovgoo.restaurantservice.service.restaurant.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.dto.restaurant.RestaurantShortResponse;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantCreateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantSearchRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantUpdateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.dto.restaurant.enums.RestaurantStatus;
import org.vovgoo.restaurantservice.exception.custom.restaurant.RestaurantNotFoundException;
import org.vovgoo.restaurantservice.mapper.RestaurantMapper;
import org.vovgoo.restaurantservice.repository.RestaurantRepository;
import org.vovgoo.restaurantservice.service.restaurant.RestaurantImageService;
import org.vovgoo.restaurantservice.service.restaurant.RestaurantService;
import org.vovgoo.user.aspect.CheckUserStatus;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantImageService restaurantImageService;
    private final RestaurantMapper restaurantMapper;

    @Override
    public PageResponse<RestaurantResponse> listRestaurants(RestaurantSearchRequest searchRequest, PageParams pageParams) {
        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());

        Page<RestaurantResponse> page = restaurantRepository.findByCuisineContainingIgnoreCase(searchRequest.cuisine(), pageRequest)
                .map(restaurantMapper::toResponse);

        return PageResponse.of(page);
    }

    @Override
    public RestaurantResponse getById(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdWithImages(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public RestaurantResponse create(RestaurantCreateRequest request) {

        Restaurant restaurant = Restaurant.builder()
                .name(request.name())
                .description(request.description())
                .cuisine(request.cuisine())
                .address(request.address())
                .website(request.website())
                .phone(request.phone())
                .openingTime(request.openingTime())
                .closingTime(request.closingTime())
                .deliveryAvailable(request.deliveryAvailable())
                .parkingAvailable(request.parkingAvailable())
                .status(RestaurantStatus.ACTIVE)
                .build();

        restaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public RestaurantResponse update(UUID restaurantId, RestaurantUpdateRequest request) {

        Restaurant restaurant = restaurantRepository.findByIdWithImages(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        restaurant.setName(request.name());
        restaurant.setDescription(request.description());
        restaurant.setCuisine(request.cuisine());
        restaurant.setAddress(request.address());
        restaurant.setWebsite(request.website());
        restaurant.setPhone(request.phone());
        restaurant.setOpeningTime(request.openingTime());
        restaurant.setClosingTime(request.closingTime());
        restaurant.setDeliveryAvailable(request.deliveryAvailable());
        restaurant.setParkingAvailable(request.parkingAvailable());
        restaurant.setStatus(request.status());

        restaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void delete(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        restaurant.setStatus(RestaurantStatus.CLOSED);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void uploadImage(UUID restaurantId, MultipartFile file) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        restaurantImageService.upload(restaurant, file, false);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void deleteImage(UUID restaurantId, UUID imageId) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        restaurantImageService.remove(restaurant, imageId, false);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void setProfileImage(UUID restaurantId, MultipartFile file) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        restaurantImageService.upload(restaurant, file, true);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void removeProfileImage(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);
        restaurantImageService.remove(restaurant, null, true);
    }

    @Override
    public RestaurantShortResponse getInternalRestaurantById(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdWithImagesIgnoreStatus(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        return restaurantMapper.toShortResponse(restaurant);
    }
}
