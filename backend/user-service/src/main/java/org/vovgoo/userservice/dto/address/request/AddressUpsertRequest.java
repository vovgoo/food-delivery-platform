package org.vovgoo.userservice.dto.address.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на создание или обновление адреса пользователя")
public record AddressUpsertRequest(

        @Schema(description = "ID адреса (для обновления существующего), если null — будет создан новый адрес", example = "1")
        Long id,

        @Schema(description = "Название улицы", example = "Ленина 10")
        @NotBlank(message = "Улица не может быть пустой")
        @Size(max = 255, message = "Слишком длинная улица")
        @Pattern(regexp = "^[\\p{L}0-9\\s\\-\\.]+$", message = "Улица содержит недопустимые символы")
        String street,

        @Schema(description = "Город", example = "Москва")
        @NotBlank(message = "Город не может быть пустым")
        @Size(max = 100, message = "Слишком длинный город")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Город содержит недопустимые символы")
        String city,

        @Schema(description = "ZIP/почтовый код", example = "101000")
        @NotBlank(message = "ZIP код не может быть пустым")
        @Size(max = 20, message = "ZIP слишком длинный")
        @Pattern(regexp = "^[0-9\\-\\s]+$", message = "ZIP содержит недопустимые символы")
        String zip,

        @Schema(description = "Штат или регион", example = "Московская область")
        @NotBlank(message = "Штат/регион не может быть пустым")
        @Size(max = 100, message = "Слишком длинный штат/регион")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Штат/регион содержит недопустимые символы")
        String state,

        @Schema(description = "Страна", example = "Россия")
        @NotBlank(message = "Страна не может быть пустой")
        @Size(max = 100, message = "Слишком длинная страна")
        @Pattern(regexp = "^[\\p{L}\\s\\-]+$", message = "Страна содержит недопустимые символы")
        String country
) {}
