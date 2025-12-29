# Kafka Event Handler – Microservice Architecture

## Overview

This project demonstrates a **generic Kafka Event Handler framework** designed using **Microservice Architecture**, **Event-Driven Architecture**, and **Hexagonal Architecture (Ports & Adapters)**.

The goal is to build a scalable and extensible Kafka consumer mechanism where:

* Multiple event types are handled dynamically
* Business logic is decoupled from Kafka infrastructure
* New event types can be added without modifying existing routing logic
* Spring Boot auto-configuration and bean processing are leveraged effectively

---

## Architecture Assumptions

Since the architecture was not explicitly mentioned, the system is designed using:

* **Microservice Architecture**
* **Apache Kafka** for asynchronous communication
* **Spring Boot & Spring Kafka**
* **Hexagonal Architecture** inside each microservice

Each microservice:

* Is independently deployable
* Owns its domain logic
* Communicates only via Kafka events

---

## High-Level System Architecture

```
┌───────────────┐      ┌───────────────┐
│ Producer MS A │      │ Producer MS B │
└───────┬───────┘      └───────┬───────┘
        │                      │
        └────────── Kafka ─────┘
                 Topic
                   │
        ┌──────────▼──────────┐
        │ Consumer Microservice│
        │ (Kafka Event Handler)│
        └─────────────────────┘
```

---

## Internal Microservice Architecture (Hexagonal)

Each microservice follows **Ports & Adapters** internally.

```
┌──────────────────────────────┐
│ Application Layer            │
│ Event Router / Dispatcher    │
└──────────────┬───────────────┘
               │
┌──────────────▼───────────────┐
│ Domain Layer                 │
│ Event Handlers (Business)    │
└──────────────┬───────────────┘
               │
┌──────────────▼───────────────┐
│ Infrastructure Layer         │
│ Kafka Listener / Producer    │
└──────────────────────────────┘
```

---

## Event Payload Design

All Kafka messages follow a **standard event envelope**.

```json
{
  "eventType": "EVENT_TYPE_1",
  "eventId": "uuid",
  "timestamp": "2025-01-01T10:30:00Z",
  "payload": {
    "data": "business specific fields"
  }
}
```

### Why this structure?

* Enables generic deserialization
* Keeps services loosely coupled
* Supports schema evolution
* Allows multiple consumers to react independently

---

## Core Domain Port

The domain layer defines a generic event handler contract.

```java
public interface EventHandler<T> {
    EventType eventType();
    void handle(T payload);
}
```

* No Kafka dependency
* No Spring dependency
* Pure business contract

---

## Domain Event Handlers

Each event type has its own handler implementation.

```java
@Component
public class EventType1Handler implements EventHandler<EventType1Payload> {

    @Override
    public EventType eventType() {
        return EventType.EVENT_TYPE_1;
    }

    @Override
    public void handle(EventType1Payload payload) {
        // Business logic only
    }
}
```

Each microservice contains **only the handlers relevant to its domain**.

---

## Spring Bean Processing & Registry

Spring automatically discovers all `EventHandler` beans and registers them.

```java
@Component
public class EventHandlerRegistry {

    private final Map<EventType, EventHandler<?>> handlers;

    public EventHandlerRegistry(List<EventHandler<?>> handlerList) {
        this.handlers = handlerList.stream()
            .collect(Collectors.toMap(
                EventHandler::eventType,
                Function.identity()
            ));
    }

    public EventHandler<?> get(EventType type) {
        return handlers.get(type);
    }
}
```

### Key Learning

* Uses Spring dependency injection
* No manual wiring
* New handlers are auto-registered

---

## Kafka Listener Adapter (Infrastructure Layer)

```java
@Component
public class KafkaEventListener {

    private final EventHandlerRegistry registry;

    @KafkaListener(topics = "events-topic")
    public void consume(GenericEvent event) {
        EventHandler handler = registry.get(event.getEventType());
        handler.handle(event.getPayload());
    }
}
```

### Responsibility

* Kafka-specific logic only
* Delegates to domain handlers
* Keeps domain clean and testable

---

## Event Flow

1. Producer microservice publishes an event to Kafka
2. Kafka stores the event in a topic
3. Consumer microservice listens to the topic
4. Event is deserialized into a generic envelope
5. Event type is identified
6. Corresponding domain handler processes the event

---

## Error Handling Strategy

Each microservice handles failures independently.

* Retryable failures → Retry topic
* Non-retryable failures → Dead Letter Topic (DLT)

```
events-topic → retry-topic → dead-letter-topic
```

This ensures resilience and fault isolation.

---

## Why This Design Works Well

| Feature                | Benefit                               |
| ---------------------- | ------------------------------------- |
| Microservices          | Independent scaling & deployment      |
| Kafka                  | Asynchronous, resilient communication |
| Hexagonal Architecture | Clean separation of concerns          |
| Spring Auto-Config     | Minimal boilerplate                   |
| Event Routing          | Easy extensibility                    |
| Domain Isolation       | Testable business logic               |

---

## Conclusion

This design provides a **scalable, extensible, and clean Kafka event handling framework** suitable for real-world microservice systems.
It demonstrates strong understanding of **Spring internals, Kafka, event-driven systems, and clean architecture principles**.

---

## Future Improvements

* Schema Registry integration (Avro/Protobuf)
* Kafka Streams for event aggregation
* Observability (metrics, tracing)
* Idempotency handling
* Saga orchestration support

---

### Author

Designed as part of a **Kafka + Spring learning activity** focusing on real-world microservice patterns.
