package org.vovgoo.dto.address;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Short response with address details")
public record AddressShortResponse(

        @Schema(description = "Unique address ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "City name", example = "Минск")
        String city,

        @Schema(description = "Street name", example = "Ленина")
        String street,

        @Schema(description = "House number", example = "12А")
        String house,

        @Schema(description = "Apartment or office", example = "кв. 45")
        String apartment
) {}