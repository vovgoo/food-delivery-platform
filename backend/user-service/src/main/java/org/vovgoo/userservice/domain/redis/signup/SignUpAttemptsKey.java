package org.vovgoo.userservice.domain.redis.signup;

import org.vovgoo.userservice.domain.redis.RedisKey;

import java.time.Duration;

public class SignUpAttemptsKey implements RedisKey<Integer> {
    private final String phone;
    private SignUpAttemptsKey(String phone){ this.phone = phone; }
    public static SignUpAttemptsKey of(String phone){ return new SignUpAttemptsKey(phone); }
    public String asString(){ return "signup:attempts:" + phone; }
    public Class<Integer> valueType(){ return Integer.class; }
    public Duration ttl(){ return Duration.ofHours(1); }
}
