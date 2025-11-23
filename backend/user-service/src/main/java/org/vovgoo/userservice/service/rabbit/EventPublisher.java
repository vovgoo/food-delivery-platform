package org.vovgoo.userservice.service.rabbit;

import org.vovgoo.userservice.domain.rabbit.key.EventKey;

public interface EventPublisher {
    <T> void publish(EventKey<T> eventKey, T payload);
}
