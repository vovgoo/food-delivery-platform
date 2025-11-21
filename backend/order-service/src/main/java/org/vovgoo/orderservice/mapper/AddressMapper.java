package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.dto.address.AddressShortResponse;
import org.vovgoo.orderservice.dto.address.response.AddressResponse;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "city", target = "city")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "house", target = "house")
    @Mapping(source = "apartment", target = "apartment")
    @Mapping(source = "addressStatus", target = "addressStatus")
    AddressResponse toResponse(AddressShortResponse address);
}
