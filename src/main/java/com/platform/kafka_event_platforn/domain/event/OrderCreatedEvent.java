package com.platform.kafka_event_platforn.domain.event;

import lombok.Data;

@Data
public class OrderCreatedEvent implements PlatformEvent {

    private String orderId;
    private double amount;

    @Override
    public String getEventType() {
        return "EVENT_TYPE_1";
    }
}

