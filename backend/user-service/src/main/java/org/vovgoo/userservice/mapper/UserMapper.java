package org.vovgoo.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.userservice.dto.user.response.UserResponse;
import org.vovgoo.userservice.entity.User;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, RoleMapper.class})
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "fullName", source = "fullName")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "addresses", source = "addresses")
    @Mapping(target = "roles", source = "roles")
    UserResponse toResponse(User user);
}
