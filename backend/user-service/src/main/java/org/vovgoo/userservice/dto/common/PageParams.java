package org.vovgoo.userservice.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Pagination parameters for requests")
public record PageParams(

        @Schema(description = "Page number (zero-based)", example = "0")
        @NotNull(message = "Номер страницы обязателен")
        @Min(value = 0, message = "Номер страницы не может быть отрицательным")
        Integer page,

        @Schema(description = "Number of items per page", example = "20")
        @NotNull(message = "Размер страницы обязателен")
        @Min(value = 0, message = "Размер страницы не может быть отрицательным")
        @Max(value = 100, message = "Размер страницы не может быть больше 100")
        Integer size

) {}
