package org.vovgoo.restaurantservice.dto.restaurant.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.vovgoo.enums.restaurant.RestaurantStatus;
import org.vovgoo.restaurantservice.validation.restaurant.status.AllowedRestaurantStatus;
import org.vovgoo.validators.phone.Phone;

import java.time.LocalTime;

@Schema(description = "Request for updating an existing restaurant")
public record RestaurantUpdateRequest(

        @Schema(description = "Restaurant name", example = "Итальянский Ресторан")
        @NotBlank(message = "Название ресторана не может быть пустым")
        @Size(min = 2, max = 100, message = "Название ресторана должно быть от 2 до 100 символов")
        String name,

        @Schema(description = "Restaurant description", example = "Обновленное описание ресторана")
        @Size(max = 1000, message = "Описание ресторана не должно превышать 1000 символов")
        String description,

        @Schema(description = "Cuisine type", example = "Итальянская")
        @NotBlank(message = "Кухня не может быть пустой")
        @Size(min = 2, max = 50, message = "Кухня должна быть от 2 до 50 символов")
        String cuisine,

        @Schema(description = "Restaurant address", example = "ул. Ленина, д. 5, Москва")
        @NotBlank(message = "Адрес не может быть пустым")
        @Size(min = 5, max = 200, message = "Адрес должен быть от 5 до 200 символов")
        String address,

        @Schema(description = "Website URL", example = "https://italian-restaurant-update.ru")
        @Size(max = 200, message = "URL сайта не должен превышать 200 символов")
        @URL(message = "Некорректный формат сайта")
        String website,

        @Schema(description = "Contact phone number", example = "+7 999 987-65-43")
        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        String phone,

        @Schema(description = "Opening time", example = "10:00")
        @NotNull(message = "Время открытия должно быть указано")
        LocalTime openingTime,

        @Schema(description = "Closing time", example = "23:00")
        @NotNull(message = "Время закрытия должно быть указано")
        LocalTime closingTime,

        @Schema(description = "Is delivery available", example = "true")
        @NotNull(message = "Необходимо указать, доступна ли доставка")
        Boolean deliveryAvailable,

        @Schema(description = "Is parking available", example = "false")
        @NotNull(message = "Необходимо указать, доступна ли парковка")
        Boolean parkingAvailable,

        @Schema(description = "Restaurant status", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE"})
        @NotNull(message = "Статус ресторана не может быть пустым")
        @AllowedRestaurantStatus(anyOf = {RestaurantStatus.ACTIVE, RestaurantStatus.INACTIVE},
                message = "Статус ресторана должен быть ACTIVE или INACTIVE")
        RestaurantStatus status
) {}
