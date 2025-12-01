package org.vovgoo.restaurantservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.common.domain.dish.dto.DishInternalResponse;
import org.vovgoo.restaurantservice.dto.dish.response.DishResponse;
import org.vovgoo.restaurantservice.dto.dish.response.DishShortResponse;
import org.vovgoo.restaurantservice.entity.Dish;
import org.vovgoo.restaurantservice.entity.Image;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface DishMapper {

    @Mapping(target = "id", source = "dish.id")
    @Mapping(target = "name", source = "dish.name")
    @Mapping(target = "description", source = "dish.description")
    @Mapping(target = "profileImage", expression = "java(images.stream().filter(Image::getIsProfile).findFirst().map(ImageMapper.INSTANCE::toResponse).orElse(null))")
    @Mapping(target = "portionInGrams", source = "dish.portionInGrams")
    @Mapping(target = "proteins", source = "dish.proteins")
    @Mapping(target = "fats", source = "dish.fats")
    @Mapping(target = "carbohydrates", source = "dish.carbohydrates")
    @Mapping(target = "spicy", source = "dish.spicy")
    @Mapping(target = "vegan", source = "dish.vegan")
    @Mapping(target = "vegetarian", source = "dish.vegetarian")
    @Mapping(target = "price", source = "dish.price")
    @Mapping(target = "status", source = "dish.status")
    @Mapping(target = "images", expression = "java(images.stream().filter(i -> !i.getIsProfile()).map(ImageMapper.INSTANCE::toResponse).toList())")
    DishResponse toResponse(Dish dish, List<Image> images);

    @Mapping(target = "id", source = "dish.id")
    @Mapping(target = "name", source = "dish.name")
    @Mapping(target = "description", source = "dish.description")
    @Mapping(target = "profileImage", source = "profileImage")
    @Mapping(target = "portionInGrams", source = "dish.portionInGrams")
    @Mapping(target = "proteins", source = "dish.proteins")
    @Mapping(target = "fats", source = "dish.fats")
    @Mapping(target = "carbohydrates", source = "dish.carbohydrates")
    @Mapping(target = "spicy", source = "dish.spicy")
    @Mapping(target = "vegan", source = "dish.vegan")
    @Mapping(target = "vegetarian", source = "dish.vegetarian")
    @Mapping(target = "price", source = "dish.price")
    @Mapping(target = "status", source = "dish.status")
    DishShortResponse toShortResponse(Dish dish, Image profileImage);

    @Mapping(target = "id", source = "dish.id")
    @Mapping(target = "name", source = "dish.name")
    @Mapping(target = "profileImageUrl", source = "profileImage.url")
    @Mapping(target = "price", source = "dish.price")
    @Mapping(target = "status", source = "dish.status")
    DishInternalResponse toInternalResponse(Dish dish, Image profileImage);
}
