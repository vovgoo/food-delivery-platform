package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.common.domain.restaurant.dto.RestaurantInternalResponse;
import org.vovgoo.orderservice.dto.restaurant.response.RestaurantResponse;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "cuisine", target = "cuisine")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "profileImageUrl", target = "profileImageUrl")
    RestaurantResponse toResponse(RestaurantInternalResponse restaurant);
}
