package org.vovgoo.userservice.domain.redis.signup;

import org.vovgoo.userservice.domain.redis.RedisKey;
import org.vovgoo.userservice.dto.security.auth.request.SignUpRequest;

import java.time.Duration;

public class SignUpRequestKey implements RedisKey<SignUpRequest> {
    private final String phone;
    private SignUpRequestKey(String phone) { this.phone = phone; }
    public static SignUpRequestKey of(String phone){ return new SignUpRequestKey(phone); }
    public String asString() { return "signup:request:" + phone; }
    public Class<SignUpRequest> valueType() { return SignUpRequest.class; }
    public Duration ttl() { return Duration.ofHours(1); }
}
