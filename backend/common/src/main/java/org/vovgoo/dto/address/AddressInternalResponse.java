package org.vovgoo.dto.address;

import org.vovgoo.enums.address.AddressStatus;

import java.util.UUID;

public record AddressInternalResponse(
        UUID id,
        String city,
        String street,
        String house,
        String apartment,
        AddressStatus addressStatus
) {}