package org.vovgoo.restaurantservice.service.restaurant.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.dto.pageable.PageParams;
import org.vovgoo.dto.pageable.PageResponse;
import org.vovgoo.dto.restaurant.RestaurantInternalResponse;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantCreateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantSearchRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantUpdateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.entity.Image;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.enums.restaurant.RestaurantStatus;
import org.vovgoo.restaurantservice.entity.enums.ImageType;
import org.vovgoo.restaurantservice.exception.custom.restaurant.RestaurantNotFoundException;
import org.vovgoo.restaurantservice.mapper.RestaurantMapper;
import org.vovgoo.restaurantservice.repository.ImageRepository;
import org.vovgoo.restaurantservice.repository.RestaurantRepository;
import org.vovgoo.restaurantservice.service.image.ImageService;
import org.vovgoo.restaurantservice.service.restaurant.RestaurantService;
import org.vovgoo.user.aspect.CheckUserStatus;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final RestaurantMapper restaurantMapper;

    @Override
    public PageResponse<RestaurantResponse> listRestaurants(RestaurantSearchRequest searchRequest, PageParams pageParams) {
        PageRequest pageRequest = PageRequest.of(pageParams.page(), pageParams.size());
        Page<Restaurant> restaurantPage = restaurantRepository.findByCuisineContainingIgnoreCase(searchRequest.cuisine(), pageRequest);

        List<UUID> pageIds = restaurantPage.map(Restaurant::getId).toList();

        List<Image> images = imageRepository.findAllByParentIdsAndType(pageIds, ImageType.RESTAURANT);

        Map<UUID, List<Image>> imagesMap = images.stream()
                .collect(Collectors.groupingBy(Image::getParentId));

        Page<RestaurantResponse> restaurantsResponses = restaurantPage
                .map(d -> restaurantMapper.toResponse(d, imagesMap.getOrDefault(d.getId(), Collections.emptyList())));

        return PageResponse.of(restaurantsResponses);
    }

    @Override
    public RestaurantResponse getById(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        List<Image> images = imageRepository.findAllByParentIdAndType(restaurantId, ImageType.RESTAURANT);

        return restaurantMapper.toResponse(restaurant, images);
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

        return restaurantMapper.toResponse(restaurant, List.of());
    }

    @Override
    @Transactional
    @CheckUserStatus
    public RestaurantResponse update(UUID restaurantId, RestaurantUpdateRequest request) {

        Restaurant restaurant = restaurantRepository.findByIdNotClosed(restaurantId)
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

        List<Image> images = imageRepository.findAllByParentIdAndType(restaurantId, ImageType.RESTAURANT);

        return restaurantMapper.toResponse(restaurant, images);
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
        restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        imageService.uploadImage(restaurantId, ImageType.RESTAURANT, file);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void deleteImage(UUID restaurantId, UUID imageId) {
        restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        imageService.removeImage(restaurantId, imageId, ImageType.RESTAURANT);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void setProfileImage(UUID restaurantId, MultipartFile file) {
        restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        imageService.uploadProfileImage(restaurantId, ImageType.RESTAURANT, file);
    }

    @Override
    @Transactional
    @CheckUserStatus
    public void removeProfileImage(UUID restaurantId) {
        restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        imageService.removeProfileImage(restaurantId, ImageType.RESTAURANT);
    }

    @Override
    public RestaurantInternalResponse getInternalRestaurantById(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdIgnoreStatus(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        Image image = imageRepository.findProfileImage(restaurantId, ImageType.RESTAURANT)
                .orElse(null);

        return restaurantMapper.toShortResponse(restaurant, image);
    }
}
