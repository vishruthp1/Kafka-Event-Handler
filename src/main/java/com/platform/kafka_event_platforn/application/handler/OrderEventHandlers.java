package com.platform.kafka_event_platforn.application.handler;

import com.platform.kafka_event_platforn.domain.event.OrderCreatedEvent;
import com.platform.kafka_event_platforn.platform.annotation.KafkaEventController;
import com.platform.kafka_event_platforn.platform.annotation.KafkaEventHandler;
import org.springframework.stereotype.Component;

@KafkaEventController
@Component   // 🔥 REQUIRED
public class OrderEventHandlers {

    @KafkaEventHandler(eventType = "EVENT_TYPE_1")
    public void handleOrderCreated(OrderCreatedEvent event) {
        System.out.println("🔥 ORDER HANDLER EXECUTED 🔥");
        System.out.println("Order received: " + event.getOrderId());
    }
}

