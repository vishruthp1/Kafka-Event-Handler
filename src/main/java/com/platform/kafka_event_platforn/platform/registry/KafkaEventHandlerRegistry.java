package com.platform.kafka_event_platforn.platform.registry;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KafkaEventHandlerRegistry {

    private final Map<String, EventHandlerMethod> handlers = new ConcurrentHashMap<>();

    public void register(String eventType, EventHandlerMethod method) {
        System.out.println("✅ Registering handler for eventType: " + eventType);
        handlers.put(eventType, method);
    }

    public EventHandlerMethod getHandler(String eventType) {
        return handlers.get(eventType);
    }
}


