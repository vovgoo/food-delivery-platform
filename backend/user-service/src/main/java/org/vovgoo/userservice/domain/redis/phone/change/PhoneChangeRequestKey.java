package org.vovgoo.userservice.domain.redis.phone.change;

import org.vovgoo.userservice.domain.redis.RedisKey;
import org.vovgoo.userservice.dto.user.request.ChangePhoneRequest;

import java.time.Duration;
import java.util.UUID;

public class PhoneChangeRequestKey implements RedisKey<ChangePhoneRequest> {
    private final UUID userId;
    private PhoneChangeRequestKey(UUID userId){ this.userId = userId; }
    public static PhoneChangeRequestKey of(UUID userId){ return new PhoneChangeRequestKey(userId); }
    public String asString(){ return "phone:change:request:" + userId; }
    public Class<ChangePhoneRequest> valueType(){ return ChangePhoneRequest.class; }
    public Duration ttl(){ return Duration.ofHours(1); }
}
