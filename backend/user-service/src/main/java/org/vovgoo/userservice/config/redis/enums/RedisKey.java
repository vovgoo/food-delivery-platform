package org.vovgoo.userservice.config.redis.enums;

import lombok.Getter;
import org.vovgoo.userservice.dto.security.auth.request.SignUpRequest;

@Getter
public enum RedisKey {
    SIGNUP_REQUEST("signup-request", SignUpRequest.class),
    PHONE_VERIFICATION_CODE("phone-verification-code", String.class),
    PHONE_VERIFICATION_RATE_LIMIT("phone-verification-rate-limit", Boolean.class),
    PHONE_VERIFICATION_ATTEMPTS("phone-verification-attempts", Integer.class),
    REFRESH_TOKEN("refresh-token", String.class);

    private final String key;
    private final Class<?> type;

    RedisKey(String key, Class<?> type) {
        this.key = key;
        this.type = type;
    }
}