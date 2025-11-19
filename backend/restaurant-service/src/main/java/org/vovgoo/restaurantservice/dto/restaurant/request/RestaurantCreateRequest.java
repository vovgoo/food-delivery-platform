package org.vovgoo.restaurantservice.dto.restaurant.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.vovgoo.validators.phone.Phone;

import java.time.LocalTime;

public record RestaurantCreateRequest(

        @NotBlank(message = "Название ресторана не может быть пустым")
        @Size(min = 2, max = 100, message = "Название ресторана должно быть от 2 до 100 символов")
        String name,

        @Size(max = 1000, message = "Описание ресторана не должно превышать 1000 символов")
        String description,

        @NotBlank(message = "Кухня не может быть пустой")
        @Size(min = 2, max = 50, message = "Кухня должна быть от 2 до 50 символов")
        String cuisine,

        @NotBlank(message = "Адрес не может быть пустым")
        @Size(min = 5, max = 200, message = "Адрес должен быть от 5 до 200 символов")
        String address,

        @Size(max = 200, message = "URL сайта не должен превышать 200 символов")
        @URL(message = "Некорректный формат сайта")
        String website,

        @Phone
        @NotBlank(message = "Телефон не может быть пустым")
        String phone,

        @NotNull(message = "Время открытия должно быть указано")
        LocalTime openingTime,

        @NotNull(message = "Время закрытия должно быть указано")
        LocalTime closingTime,

        @NotNull(message = "Необходимо указать, доступна ли доставка")
        Boolean deliveryAvailable,

        @NotNull(message = "Необходимо указать, доступна ли парковка")
        Boolean parkingAvailable
) {}
