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

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "cuisine", source = "cuisine")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "website", source = "website")
    @Mapping(target = "profileImage", expression = "java(images.stream().filter(Image::getIsProfile).findFirst().map(ImageMapper.INSTANCE::toResponse).orElse(null))")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "openingTime", source = "openingTime")
    @Mapping(target = "closingTime", source = "closingTime")
    @Mapping(target = "deliveryAvailable", source = "deliveryAvailable")
    @Mapping(target = "parkingAvailable", source = "parkingAvailable")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "images", expression = "java(images.stream().filter(i -> !i.getIsProfile()).map(ImageMapper.INSTANCE::toResponse).toList())")
    RestaurantResponse toResponse(Restaurant restaurant, List<Image> images);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "cuisine", source = "cuisine")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "profileImageUrl", source = "profileImage.url")
    @Mapping(target = "status", source = "status")
    RestaurantInternalResponse toShortResponse(Restaurant restaurant, Image profileImage);
}