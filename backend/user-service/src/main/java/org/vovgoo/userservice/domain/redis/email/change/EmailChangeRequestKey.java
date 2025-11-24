package org.vovgoo.userservice.domain.redis.email.change;

import org.vovgoo.userservice.domain.redis.RedisKey;
import org.vovgoo.userservice.dto.user.request.ChangeEmailRequest;

import java.time.Duration;
import java.util.UUID;

public class EmailChangeRequestKey implements RedisKey<ChangeEmailRequest> {
    private final UUID userId;
    private EmailChangeRequestKey(UUID userId){ this.userId = userId; }
    public static EmailChangeRequestKey of(UUID  userId){ return new EmailChangeRequestKey(userId); }
    public String asString(){ return "email:change:request:" + userId; }
    public Class<ChangeEmailRequest> valueType(){ return ChangeEmailRequest.class; }
    public Duration ttl(){ return Duration.ofHours(1); }
}
