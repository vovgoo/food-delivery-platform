package org.vovgoo.userservice.config.redis;

import lombok.Getter;
import org.vovgoo.userservice.dto.security.auth.request.SignUpRequest;
import org.vovgoo.userservice.dto.user.request.ChangeEmailRequest;
import org.vovgoo.userservice.dto.user.request.ChangePhoneRequest;

import java.time.Duration;

public enum RedisKey {

    SIGNUP_REQUEST("signup:request:%s", Duration.ofHours(1), SignUpRequest.class),

    PHONE_VERIFICATION_CODE("phone:verification:%s:code:%s", Duration.ofHours(1), String.class),
    PHONE_VERIFICATION_RATE_LIMIT("phone:verification:%s:rate-limit:%s", Duration.ofMinutes(1), Boolean.class),
    PHONE_VERIFICATION_ATTEMPTS("phone:verification:%s:attempts:%s", Duration.ofHours(1), Integer.class),

    EMAIL_VERIFICATION_LINK("email:verification:%s:link:%s", Duration.ofHours(1), String.class),
    EMAIL_VERIFICATION_ATTEMPTS("email:verification:%s:attempts:%s", Duration.ofMinutes(1), Integer.class),

    REFRESH_TOKEN("refresh:token:%s", Duration.ofDays(15), String.class),

    PHONE_CHANGE_REQUEST("phone:change:%s:%s", Duration.ofHours(1), ChangePhoneRequest.class),
    EMAIL_CHANGE_REQUEST("email:change:%s:%s", Duration.ofHours(1), ChangeEmailRequest.class);

    private final String pattern;

    @Getter
    private final Duration ttl;

    @Getter
    private final Class<?> valueType;

    RedisKey(String pattern, Duration ttl, Class<?> valueType) {
        this.pattern = pattern;
        this.ttl = ttl;
        this.valueType = valueType;
    }

    public String buildKey(String... args) {
        return String.format(pattern, (Object[]) args);
    }
}
