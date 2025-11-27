package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.common.domain.user.dto.UserInternalResponse;
import org.vovgoo.orderservice.dto.user.response.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "birthDate", target = "birthDate")
    @Mapping(source = "userStatus", target = "userStatus")
    UserResponse toResponse(UserInternalResponse restaurant);
}
