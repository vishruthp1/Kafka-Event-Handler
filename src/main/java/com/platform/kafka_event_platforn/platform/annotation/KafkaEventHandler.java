package com.platform.kafka_event_platforn.platform.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KafkaEventHandler {
    String eventType();
}

