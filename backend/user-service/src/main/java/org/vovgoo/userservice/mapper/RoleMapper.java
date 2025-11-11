package org.vovgoo.userservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.userservice.dto.role.response.RoleResponse;
import org.vovgoo.userservice.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "name", source = "name")
    RoleResponse toResponse(Role role);
}
