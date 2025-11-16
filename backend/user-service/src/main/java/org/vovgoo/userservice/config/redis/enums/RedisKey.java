package org.vovgoo.userservice.config.redis.enums;

import lombok.Getter;

@Getter
public enum RedisKey {
    SIGNUP_REQUEST("signup-request"),
    PHONE_VERIFICATION_CODE("phone-verification-code"),
    PHONE_VERIFICATION_RATE_LIMIT("phone-verification-rate-limit"),
    PHONE_VERIFICATION_ATTEMPTS("phone-verification-attempts");

    private final String key;

    RedisKey(String key) {
        this.key = key;
    }
}
