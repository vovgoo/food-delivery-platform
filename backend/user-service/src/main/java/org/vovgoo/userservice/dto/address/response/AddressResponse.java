package org.vovgoo.userservice.dto.address.response;

import java.util.UUID;

public record AddressResponse(
    UUID id,
    String country,
    String state,
    String city,
    String street,
    String house,
    String building,
    String apartment,
    String deliveryInstructions,
    String zip,
    boolean isDefault
) { }
