package org.vovgoo.userservice.exception.custom.role;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException() {
        super("Роль не найдена");
    }
}
