package org.vovgoo.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.common.domain.user.dto.UserInternalResponse;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.entity.Address;
import org.vovgoo.userservice.entity.User;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "defaultAddress", source = "defaultAddress")
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "birthDate", source = "user.birthDate")
    @Mapping(target = "userStatus", source = "user.status")
    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "updatedAt", source = "user.updatedAt")
    @Mapping(target = "roles", source = "user.roles")
    UserResponse toResponse(User user, Address defaultAddress);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "birthDate", source = "user.birthDate")
    @Mapping(target = "userStatus", source = "user.status")
    UserInternalResponse toInternalResponse(User user);
}
