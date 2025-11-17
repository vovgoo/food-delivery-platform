package org.vovgoo.userservice.dto.address.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAddressRequest(

        @NotBlank(message = "Страна не может быть пустой")
        @Size(max = 100, message = "Слишком длинная страна")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Страна содержит недопустимые символы")
        String country,

        @NotBlank(message = "Штат/регион не может быть пустым")
        @Size(max = 100, message = "Слишком длинный штат/регион")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Штат/регион содержит недопустимые символы")
        String state,

        @NotBlank(message = "Город не может быть пустым")
        @Size(max = 100, message = "Слишком длинный город")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Город содержит недопустимые символы")
        String city,

        @NotBlank(message = "Улица не может быть пустой")
        @Size(max = 255, message = "Слишком длинная улица")
        @Pattern(regexp = "^[\\p{L}0-9\\s\\-\\.]+$", message = "Улица содержит недопустимые символы")
        String street,

        @NotBlank(message = "Номер дома обязателен")
        @Size(max = 20, message = "Номер дома слишком длинный")
        @Pattern(regexp = "^[0-9A-Za-z\\-\\/]+$", message = "Номер дома содержит недопустимые символы")
        String house,

        @Size(max = 10, message = "Слишком длинный корпус")
        @Pattern(regexp = "^[0-9A-Za-z\\-\\/]*$", message = "Корпус содержит недопустимые символы")
        String building,

        @Size(max = 10, message = "Слишком длинная квартира/офис")
        @Pattern(regexp = "^[0-9A-Za-z\\-\\/]*$", message = "Квартира содержит недопустимые символы")
        String apartment,

        @Size(max = 500, message = "Инструкция слишком длинная")
        String deliveryInstructions,

        @NotBlank(message = "ZIP код не может быть пустым")
        @Size(max = 20, message = "ZIP слишком длинный")
        @Pattern(regexp = "^[0-9\\-\\s]+$", message = "ZIP содержит недопустимые символы")
        String zip
) {}
