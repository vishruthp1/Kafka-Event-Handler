package com.platform.kafka_event_platforn.adapter.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.kafka_event_platforn.platform.registry.EventHandlerMethod;
import com.platform.kafka_event_platforn.platform.registry.KafkaEventHandlerRegistry;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CentralKafkaListener {

    private final KafkaEventHandlerRegistry registry;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CentralKafkaListener(KafkaEventHandlerRegistry registry) {
        this.registry = registry;
    }

    @KafkaListener(topics = "platform-events", groupId = "platform-group")
    public void listen(ConsumerRecord<String, String> record) throws Exception {

        System.out.println("==== KAFKA MESSAGE RECEIVED ====");
        System.out.println("Payload: " + record.value());

        String eventType = new String(
                record.headers().lastHeader("eventType").value()
        );

        System.out.println("Event type: " + eventType);

        EventHandlerMethod handler = registry.getHandler(eventType);

        if (handler == null) {
            System.out.println("❌ No handler registered for " + eventType);
            return;
        }

        Object payload = objectMapper.readValue(
                record.value(),
                handler.getPayloadType()
        );

        handler.getMethod().invoke(handler.getBean(), payload);
    }
}
