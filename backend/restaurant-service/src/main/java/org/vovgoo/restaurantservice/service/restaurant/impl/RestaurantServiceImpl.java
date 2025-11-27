package org.vovgoo.restaurantservice.service.restaurant.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.vovgoo.common.domain.dto.pageable.PageParams;
import org.vovgoo.common.domain.dto.pageable.PageResponse;
import org.vovgoo.common.domain.image.enums.ImageType;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;
import org.vovgoo.common.domain.restaurant.enums.RestaurantStatus;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantCreateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantSearchRequest;
import org.vovgoo.restaurantservice.dto.restaurant.request.RestaurantUpdateRequest;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.entity.Image;
import org.vovgoo.restaurantservice.entity.Restaurant;
import org.vovgoo.restaurantservice.exception.custom.restaurant.RestaurantNotFoundException;
import org.vovgoo.restaurantservice.mapper.RestaurantMapper;
import org.vovgoo.restaurantservice.repository.ImageRepository;
import org.vovgoo.restaurantservice.repository.RestaurantRepository;
import org.vovgoo.restaurantservice.service.image.facade.ImageFacadeService;
import org.vovgoo.restaurantservice.service.restaurant.RestaurantService;

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
    private final ImageFacadeService imageFacadeService;
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
    public void delete(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        restaurant.setStatus(RestaurantStatus.CLOSED);
    }

    @Override
    @Transactional
    public void uploadImage(UUID restaurantId, MultipartFile file) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        imageFacadeService.uploadImage(restaurant, file);
    }

    @Override
    @Transactional
    public void deleteImage(UUID restaurantId, UUID imageId) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        imageFacadeService.removeImage(restaurant, imageId);
    }

    @Override
    @Transactional
    public void setProfileImage(UUID restaurantId, MultipartFile file) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        imageFacadeService.uploadProfileImage(restaurant, file);
    }

    @Override
    @Transactional
    public void removeProfileImage(UUID restaurantId) {
        Restaurant restaurant = restaurantRepository.findByIdAndStatusNotClosed(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

        imageFacadeService.removeProfileImage(restaurant);
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
