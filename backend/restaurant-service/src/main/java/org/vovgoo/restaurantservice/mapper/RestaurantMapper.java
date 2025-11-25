package org.vovgoo.restaurantservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.dto.restaurant.RestaurantInternalResponse;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.entity.Image;
import org.vovgoo.restaurantservice.entity.Restaurant;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface RestaurantMapper {

    @Mapping(target = "id", source = "restaurant.id")
    @Mapping(target = "name", source = "restaurant.name")
    @Mapping(target = "description", source = "restaurant.description")
    @Mapping(target = "cuisine", source = "restaurant.cuisine")
    @Mapping(target = "address", source = "restaurant.address")
    @Mapping(target = "website", source = "restaurant.website")
    @Mapping(target = "profileImage", expression = "java(images.stream().filter(Image::getIsProfile).findFirst().map(ImageMapper.INSTANCE::toResponse).orElse(null))")
    @Mapping(target = "phone", source = "restaurant.phone")
    @Mapping(target = "openingTime", source = "restaurant.openingTime")
    @Mapping(target = "closingTime", source = "restaurant.closingTime")
    @Mapping(target = "deliveryAvailable", source = "restaurant.deliveryAvailable")
    @Mapping(target = "parkingAvailable", source = "restaurant.parkingAvailable")
    @Mapping(target = "status", source = "restaurant.status")
    @Mapping(target = "images", expression = "java(images.stream().filter(i -> !i.getIsProfile()).map(ImageMapper.INSTANCE::toResponse).toList())")
    RestaurantResponse toResponse(Restaurant restaurant, List<Image> images);

    @Mapping(target = "id", source = "restaurant.id")
    @Mapping(target = "name", source = "restaurant.name")
    @Mapping(target = "cuisine", source = "restaurant.cuisine")
    @Mapping(target = "address", source = "restaurant.address")
    @Mapping(target = "phone", source = "restaurant.phone")
    @Mapping(target = "profileImageUrl", source = "profileImage.url")
    @Mapping(target = "status", source = "restaurant.status")
    RestaurantInternalResponse toShortResponse(Restaurant restaurant, Image profileImage);
}