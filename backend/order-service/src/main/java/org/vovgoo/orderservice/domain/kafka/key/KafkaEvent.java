package org.vovgoo.orderservice.domain.kafka.key;

public class KafkaEvent<T> {

    private final String topic;
    private final Class<T> payloadType;

    public KafkaEvent(String topic, Class<T> payloadType) {
        this.topic = topic;
        this.payloadType = payloadType;
    }

    public String topic() {
        return topic;
    }

    public Class<T> payloadType() {
        return payloadType;
    }
}
