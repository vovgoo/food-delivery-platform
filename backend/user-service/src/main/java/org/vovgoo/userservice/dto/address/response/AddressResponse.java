package org.vovgoo.userservice.dto.address.response;

public record AddressResponse(
        Long id,
        String street,
        String city,
        String zip,
        String state,
        String country
) { }
