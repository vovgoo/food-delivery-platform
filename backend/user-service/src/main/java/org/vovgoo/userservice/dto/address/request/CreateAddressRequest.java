package org.vovgoo.userservice.dto.address.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Request for creating a new address")
public record CreateAddressRequest(

        @Schema(description = "Country name", example = "Россия")
        @NotBlank(message = "Страна не может быть пустой")
        @Size(max = 100, message = "Слишком длинная страна")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Страна содержит недопустимые символы")
        String country,

        @Schema(description = "State or region", example = "Минская")
        @NotBlank(message = "Штат/регион не может быть пустым")
        @Size(max = 100, message = "Слишком длинный штат/регион")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Штат/регион содержит недопустимые символы")
        String state,

        @Schema(description = "City name", example = "Минск")
        @NotBlank(message = "Город не может быть пустым")
        @Size(max = 100, message = "Слишком длинный город")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Город содержит недопустимые символы")
        String city,

        @Schema(description = "Street name", example = "Ленина")
        @NotBlank(message = "Улица не может быть пустой")
        @Size(max = 255, message = "Слишком длинная улица")
        @Pattern(regexp = "^[\\p{L}0-9\\s\\-\\.]+$", message = "Улица содержит недопустимые символы")
        String street,

        @Schema(description = "House number", example = "12А")
        @NotBlank(message = "Номер дома обязателен")
        @Size(max = 20, message = "Номер дома слишком длинный")
        @Pattern(regexp = "^[0-9\\p{L}\\-\\/]+$", message = "Номер дома содержит недопустимые символы")
        String house,

        @Schema(description = "Building/Block", example = "1")
        @Size(max = 10, message = "Слишком длинный корпус")
        @Pattern(regexp = "^[0-9A-Za-z\\-\\/]*$", message = "Корпус содержит недопустимые символы")
        String building,

        @Schema(description = "Apartment or office", example = "45")
        @Size(max = 10, message = "Слишком длинная квартира/офис")
        @Pattern(regexp = "^[0-9A-Za-z\\-\\/]*$", message = "Квартира содержит недопустимые символы")
        String apartment,

        @Schema(description = "Delivery instructions", example = "Оставить у двери")
        @Size(max = 500, message = "Инструкция слишком длинная")
        String deliveryInstructions,

        @Schema(description = "ZIP code", example = "220030")
        @NotBlank(message = "ZIP код не может быть пустым")
        @Size(max = 20, message = "ZIP слишком длинный")
        @Pattern(regexp = "^[0-9\\-\\s]+$", message = "ZIP содержит недопустимые символы")
        String zip
) {}
