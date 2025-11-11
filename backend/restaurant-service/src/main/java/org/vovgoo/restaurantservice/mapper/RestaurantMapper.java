package org.vovgoo.restaurantservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.restaurantservice.dto.restaurant.response.RestaurantResponse;
import org.vovgoo.restaurantservice.entity.Restaurant;

@Mapper(componentModel = "spring", uses = {DishMapper.class})
public interface RestaurantMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "cuisine", source = "cuisine")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "dishes", source = "dishes")
    RestaurantResponse toResponse(Restaurant restaurant);
}