package com.platform.kafka_event_platforn.adapter.kafka;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produce")
public class KafkaProducerController {

    private static final String TOPIC = "platform-events";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/{eventType}")
    public ResponseEntity<String> produceEvent(
            @PathVariable String eventType,
            @RequestBody String payload
    ) {

        Message<String> message = MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .setHeader("eventType", eventType) // 🔥 VERY IMPORTANT
                .build();

        kafkaTemplate.send(message);

        return ResponseEntity.ok("Event published successfully");
    }
}

