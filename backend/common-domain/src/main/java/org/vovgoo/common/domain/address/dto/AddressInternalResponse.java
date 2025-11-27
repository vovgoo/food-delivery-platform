package org.vovgoo.common.domain.address.dto;

import org.vovgoo.common.domain.address.enums.AddressStatus;

import java.util.UUID;

public record AddressInternalResponse(
        UUID id,
        String city,
        String street,
        String house,
        String apartment,
        AddressStatus addressStatus
) {}
