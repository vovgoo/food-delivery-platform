package org.vovgoo.restaurantservice.dto.restaurant.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RestaurantUpdateRequest(

        @NotBlank(message = "Название ресторана не может быть пустым")
        @Size(min = 2, max = 100, message = "Название ресторана должно быть от 2 до 100 символов")
        String name,

        @NotBlank(message = "Кухня не может быть пустой")
        @Size(min = 2, max = 50, message = "Кухня должна быть от 2 до 50 символов")
        String cuisine,

        @NotBlank(message = "Адрес не может быть пустым")
        @Size(min = 5, max = 200, message = "Адрес должен быть от 5 до 200 символов")
        String address
) {}
