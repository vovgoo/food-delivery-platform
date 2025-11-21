package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.dto.dish.DishShortResponse;
import org.vovgoo.orderservice.dto.dish.response.DishResponse;

@Mapper(componentModel = "spring")
public interface DishMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "profileImageUrl", target = "profileImageUrl")
    DishResponse toResponse(DishShortResponse dish);
}
