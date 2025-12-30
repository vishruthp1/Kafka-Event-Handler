# Kafka Event Handler Platform (Spring Boot)

This project demonstrates a **platform-engineered Kafka event handling framework** built on **Spring Boot + Spring Kafka**.

It eliminates `if-else` / `switch` based event routing by using **custom annotations**, **startup scanning**, and a **central Kafka dispatcher**, allowing developers to write **clean, type-safe event handlers** without Kafka boilerplate.

---

## 🚀 What This Project Does

* Uses **a single Kafka topic** with **multiple event types**
* Routes events to handlers using **custom annotations**
* Hides Kafka internals from application developers
* Supports sending events **via Postman**
* Follows **hexagonal architecture** principles

---

## 🏗️ Architecture Overview

```
Postman
  ↓
REST Producer (Spring)
  ↓
Kafka Topic (platform-events)
  ↓
Central Kafka Listener
  ↓
Event Handler Registry
  ↓
@KafkaEventHandler methods
```

---

## 📦 Tech Stack

* Java 17
* Spring Boot 3.x
* Spring Kafka
* Apache Kafka
* Docker & Docker Compose
* Postman
* IntelliJ IDEA

---

## ⚙️ Prerequisites

Make sure you have:

* Java 17+
* Docker Desktop (running)
* IntelliJ IDEA
* Postman

---

## ▶️ How to Run the Project

### 1️⃣ Start Kafka using Docker

From the project root (where `docker-compose.yml` is located):

```bash
docker-compose up -d
```

Verify containers are running:

```bash
docker ps
```

You should see:

* `zookeeper`
* `kafka`
* `kafka-ui`

---

### 2️⃣ Run the Spring Boot Application

#### Option A: IntelliJ (Recommended)

* Open `KafkaEventPlatformApplication.java`
* Click ▶ **Run**

#### Option B: Terminal

```bash
mvn clean spring-boot:run
```

Wait until you see:

```
Started KafkaEventPlatformApplication
```

---

## 📤 Send Events Using Postman (ONLY WAY)

Kafka does **not** accept HTTP directly.
Postman sends events via a **REST → Kafka producer API**.

---

### 🔹 Postman Request

**Method**

```
POST
```

**URL**

```
http://localhost:8080/produce/EVENT_TYPE_1
```

**Headers**

```
Content-Type: application/json
```

**Body (raw JSON)**

```json
{
  "orderId": "ORDER-POSTMAN-1",
  "amount": 1200
}
```

---

### 🔹 Expected Postman Response

```text
Event published successfully
```

✅ This confirms the event was **successfully sent to Kafka**.

---

## 📥 How to Confirm Event Consumption

Kafka is **asynchronous**, so Postman will **never** show consumer output.

### Check IntelliJ Console Logs

You should see:

```
==== KAFKA MESSAGE RECEIVED ====
Payload: {"orderId":"ORDER-POSTMAN-1","amount":1200}
Event type: EVENT_TYPE_1
🔥 ORDER HANDLER EXECUTED 🔥
Order received: ORDER-POSTMAN-1
```

This confirms:

* Event reached Kafka
* Consumer read the event
* Correct handler was executed

---

## 👀 How to View Events in Kafka

### Kafka UI (Recommended)

Open in browser:

```
http://localhost:8085
```

Navigate:

```
Clusters → local-kafka → Topics → platform-events → Messages
```

You can see:

* Event payload
* Headers (`eventType`)
* Partition & offset

