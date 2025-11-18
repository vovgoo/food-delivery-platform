package org.vovgoo.restaurantservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.entity.Restaurant;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface RestaurantMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "cuisine", source = "cuisine")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "website", source = "website")
    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "openingTime", source = "openingTime")
    @Mapping(target = "closingTime", source = "closingTime")
    @Mapping(target = "deliveryAvailable", source = "deliveryAvailable")
    @Mapping(target = "parkingAvailable", source = "parkingAvailable")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "images", source = "images")
    RestaurantResponse toResponse(Restaurant restaurant);
}