package org.vovgoo.userservice.dto.role.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response containing role information")
public record RoleResponse(

        @Schema(description = "Role name", example = "USER")
        String name
) {}
