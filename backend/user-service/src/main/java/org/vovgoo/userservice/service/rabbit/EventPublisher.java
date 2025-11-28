package org.vovgoo.userservice.service.rabbit;

import org.vovgoo.common.event.key.RabbitEventKey;

public interface EventPublisher {
    <T> void publish(RabbitEventKey<T> eventKey, T payload);
}
