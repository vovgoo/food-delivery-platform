package org.vovgoo.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.userservice.dto.address.response.AddressResponse;
import org.vovgoo.userservice.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "street", source = "street")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "zip", source = "zip")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "country", source = "country")
    AddressResponse toResponse(Address address);
}
