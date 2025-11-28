package org.vovgoo.common.event.key;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KafkaEventKey<T> {
    private final String topic;
    private final Class<T> payloadType;
}
