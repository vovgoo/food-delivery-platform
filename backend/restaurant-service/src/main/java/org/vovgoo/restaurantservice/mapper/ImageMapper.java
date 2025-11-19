package org.vovgoo.restaurantservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.vovgoo.restaurantservice.dto.image.response.ImageResponse;
import org.vovgoo.restaurantservice.entity.DishImage;
import org.vovgoo.restaurantservice.entity.RestaurantImage;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "url", source = "url")
    ImageResponse toResponse(DishImage dishImage);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "url", source = "url")
    ImageResponse toResponse(RestaurantImage restaurantImage);
}
