package org.vovgoo.userservice.domain.redis.signup;

import org.vovgoo.userservice.domain.redis.RedisKey;

import java.time.Duration;

public class SignUpCodeKey implements RedisKey<String> {
    private final String phone;
    private SignUpCodeKey(String phone) { this.phone = phone; }
    public static SignUpCodeKey of(String phone){ return new SignUpCodeKey(phone); }
    public String asString() { return "signup:code:" + phone; }
    public Class<String> valueType() { return String.class; }
    public Duration ttl() { return Duration.ofHours(1); }
}
