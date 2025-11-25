package org.vovgoo.restaurantservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.dto.dish.DishInternalResponse;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.restaurantservice.entity.Dish;
import org.vovgoo.restaurantservice.entity.Image;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface DishMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "profileImage", expression = "java(images.stream().filter(Image::getIsProfile).findFirst().map(ImageMapper.INSTANCE::toResponse).orElse(null))")
    @Mapping(target = "portionInGrams", source = "portionInGrams")
    @Mapping(target = "proteins", source = "proteins")
    @Mapping(target = "fats", source = "fats")
    @Mapping(target = "carbohydrates", source = "carbohydrates")
    @Mapping(target = "spicy", source = "spicy")
    @Mapping(target = "vegan", source = "vegan")
    @Mapping(target = "vegetarian", source = "vegetarian")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "images", expression = "java(images.stream().filter(i -> !i.getIsProfile()).map(ImageMapper.INSTANCE::toResponse).toList())")
    DishResponse toResponse(Dish dish, List<Image> images);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "profileImageUrl", source = "profileImage.url")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "status", source = "status")
    DishInternalResponse toShortResponse(Dish dish, Image profileImage);
}
