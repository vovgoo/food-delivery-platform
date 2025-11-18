package org.vovgoo.userservice.service.security.jwt.enums;

public enum JwtTokenType {
    ACCESS("access"),
    REFRESH("refresh");

    private final String type;

    JwtTokenType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
