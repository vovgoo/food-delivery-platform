package org.vovgoo.userservice.dto.address.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Response with address details")
public record AddressResponse(

        @Schema(description = "Unique address ID", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Country name", example = "Россия")
        String country,

        @Schema(description = "State or region", example = "Минская область")
        String state,

        @Schema(description = "City name", example = "Минск")
        String city,

        @Schema(description = "Street name", example = "Ленина")
        String street,

        @Schema(description = "House number", example = "12А")
        String house,

        @Schema(description = "Building/Block", example = "корпус 1")
        String building,

        @Schema(description = "Apartment or office", example = "кв. 45")
        String apartment,

        @Schema(description = "Delivery instructions", example = "Оставить у двери")
        String deliveryInstructions,

        @Schema(description = "ZIP code", example = "220030")
        String zip,

        @Schema(description = "Is this the default address", example = "true")
        boolean isDefault
) {}
