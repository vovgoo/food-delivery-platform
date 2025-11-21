package org.vovgoo.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.dto.address.AddressShortResponse;
import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "street", source = "street")
    @Mapping(target = "house", source = "house")
    @Mapping(target = "building", source = "building")
    @Mapping(target = "apartment", source = "apartment")
    @Mapping(target = "deliveryInstructions", source = "deliveryInstructions")
    @Mapping(target = "zip", source = "zip")
    @Mapping(target = "isDefault", source = "default")
    AddressResponse toResponse(Address address);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "street", source = "street")
    @Mapping(target = "house", source = "house")
    @Mapping(target = "apartment", source = "apartment")
    @Mapping(target = "addressStatus", source = "addressStatus")
    AddressShortResponse toShortResponse(Address address);
}
