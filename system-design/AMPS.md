<!-- File: AMPS.md -->

# AMPS High-Level Design

This document describes the **high-level architecture and runtime behavior of AMPS (Advanced Message Processing System)**. It focuses on how AMPS handles publishers, consumers, SOW (State of the World), delta messaging, filtering, scaling, and failure recovery while maintaining extremely low latency.

---

# 1. Overview

AMPS is a **high-performance messaging platform** designed for systems that require:

* extremely low latency
* very high throughput
* real-time state distribution
* efficient filtering
* scalable publish/subscribe communication

It is commonly used in:

* trading systems
* market data distribution
* real-time analytics pipelines
* telemetry ingestion
* distributed state synchronization systems

Key capabilities include:

* topic-based messaging
* publish / subscribe model
* stateful messaging with **SOW**
* delta messaging
* SQL-like filtering
* in-memory processing for ultra-low latency
* durable logging and fast recovery

---

# 2. Core Components

## 2.1 Producers

Producers publish messages into AMPS topics.

Typical producer responsibilities:

* publish order updates
* publish market data
* publish telemetry events
* publish system state changes

Example message:

```
topic: orders
message: {orderId:1001, price:101.5, status:"OPEN"}
```

Producers connect using AMPS client libraries and send messages using the **publish command**.

Producers do not communicate directly with consumers.
AMPS handles all routing and delivery.

---

## 2.2 Consumers

Consumers subscribe to topics or query the SOW to retrieve current state.

Consumers may:

* subscribe to real-time messages
* query the SOW snapshot
* subscribe with filters
* request delta updates

Example subscription:

```
subscribe topic=orders
```

Filtered subscription:

```
subscribe topic=orders filter="/price > 100"
```

---

## 2.3 Topics

A **topic** is a logical stream of messages.

Example topics:

```
orders
market_data
trades
positions
```

Messages published to a topic are routed to subscribers.

Topics can optionally maintain a **State of the World (SOW)**.

---

# 3. State of the World (SOW)

SOW is an **in-memory key-value store** maintained by AMPS.

It stores the **latest state for each key** in a topic.

Example SOW table:

| orderId | price | status |
| ------- | ----- | ------ |
| 1001    | 101.5 | OPEN   |
| 1002    | 99.0  | FILLED |

Properties:

* stored primarily in **memory**
* optimized for fast lookup
* indexed for filtering
* periodically persisted to disk
* rebuilt during recovery

Each record is identified by a **key**.

Example configuration concept:

```
topic: orders
key: orderId
```

---

# 4. Message Persistence

AMPS maintains a **transaction log** to ensure durability.

The log records:

* publish events
* delta updates
* deletes
* SOW updates

Logs allow:

* crash recovery
* replication (if configured)
* event replay

Log writes occur **asynchronously** to avoid blocking the critical message path.

---

# 5. Message Processing Pipeline

When a producer publishes a message, AMPS processes it through the following pipeline:

```
Producer
   ↓
Network Receive
   ↓
Message Decode
   ↓
SOW Lookup / Update
   ↓
Delta Merge (if needed)
   ↓
Subscription Matching
   ↓
Fan-out to Consumers
   ↓
Async Log Write
```

All performance-critical operations occur **in memory**.

---

# 6. Latency Characteristics

AMPS achieves extremely low latency due to several architectural optimizations.

Typical internal processing latency:

```
5 – 20 microseconds
```

In optimized environments this can approach **sub-microsecond internal processing**, though overall latency typically depends on network overhead.

Key design decisions:

### In-memory architecture

* SOW tables
* subscription lists
* filter indexes

All reside in RAM.

---

### Lock-free queues

AMPS minimizes locking to reduce thread contention.

---

### Zero-copy fan-out

Messages are referenced rather than copied when delivered to multiple consumers.

---

### Efficient filter indexes

Filters are compiled into fast lookup structures instead of evaluated sequentially.

---

### Asynchronous persistence

Disk I/O happens outside the message routing path.

---

# 7. Publish Scenarios

## 7.1 Standard Publish

Producer sends a full message.

Example:

```
publish topic=orders
{orderId:1001, price:101.5, status:"OPEN"}
```

Processing steps:

1. Message received
2. Topic identified
3. SOW updated
4. Filters evaluated
5. Message delivered to subscribers
6. Event written to log asynchronously

---

## 7.2 Delta Publish

Delta publish sends **only the changed fields**.

Example delta:

```
{price:102.0}
```

Existing SOW record:

```
{orderId:1001, price:101.5, status:"OPEN"}
```

After delta merge:

```
{orderId:1001, price:102.0, status:"OPEN"}
```

Benefits:

* reduced bandwidth
* smaller messages
* higher throughput

The reconstructed full message is used for routing.

---

# 8. SOW Query Scenarios

## 8.1 Basic SOW Query

Consumer requests the full current state.

```
sow topic=orders
```

Process:

1. SOW table accessed
2. Records streamed to consumer
3. Query completes

---

## 8.2 SOW Query With Filter

Example:

```
sow topic=orders filter="/price > 100"
```

Steps:

1. filter parsed
2. index lookup executed
3. matching records returned

Indexes significantly reduce query cost.

---

# 9. Subscription Scenarios

## 9.1 Subscribe Without Filter

```
subscribe topic=orders
```

Consumer receives every message published to the topic.

---

## 9.2 Subscribe With Filter

```
subscribe topic=orders filter="/price > 100"
```

AMPS evaluates the filter for each incoming message.

Only matching messages are delivered.

---

# 10. SOW + Subscribe Scenarios

## 10.1 sow_and_subscribe (No Filter)

```
sow_and_subscribe topic=orders
```

Sequence:

1. AMPS returns SOW snapshot
2. Subscription established
3. Future updates streamed to the client

Consumer receives:

```
snapshot records
followed by
live updates
```

---

## 10.2 sow_and_subscribe With Filter

```
sow_and_subscribe topic=orders filter="/symbol = 'AAPL'"
```

Behavior:

1. filtered SOW snapshot returned
2. filtered live updates streamed

---

# 11. Delta Subscription Scenarios

Consumers may request delta messages instead of full records.

Example:

```
subscribe topic=orders delta=true
```

Without delta:

```
{orderId:1001, price:102.0, status:"OPEN"}
```

With delta:

```
{price:102.0}
```

Delta subscriptions can also include filters.

Example:

```
subscribe topic=orders filter="/price > 100" delta=true
```

---

# 12. Failure Recovery

AMPS recovers using two mechanisms.

## SOW Snapshot

Periodic snapshot of the SOW written to disk.

## Transaction Log

Every event recorded in the log.

Recovery process:

```
Server start
   ↓
Load SOW snapshot
   ↓
Replay transaction log
   ↓
Rebuild in-memory state
   ↓
Resume operation
```

This guarantees consistent state restoration.

Recovery time typically ranges from **seconds to a few minutes**, depending on log size.

---

# 13. Scaling Model

AMPS primarily scales **vertically**.

Typical production hardware:

```
64 – 256 CPU cores
256GB – 1TB RAM
NVMe storage
25–100Gb network
```

Approximate capacity ranges:

| Metric                 | Typical Range         |
| ---------------------- | --------------------- |
| publishes/sec          | 100K – 1M+            |
| message deliveries/sec | tens of millions      |
| active subscriptions   | hundreds of thousands |
| SOW records            | millions              |

---

# 14. Horizontal Scaling

Multiple AMPS servers can be deployed using **topic partitioning**.

Example:

```
orders_A-M → AMPS1
orders_N-Z → AMPS2
```

Producers route messages to the appropriate server.

Consumers subscribe to the relevant AMPS node.

This approach enables horizontal scaling when workloads exceed a single server's capacity.

---

# 15. Example End-to-End Flow

Order update workflow:

```
Order Service
    ↓
Publish order update
    ↓
AMPS receives message
    ↓
SOW updated
    ↓
Subscription filters evaluated
    ↓
Message delivered to subscribers
    ↓
Event written to transaction log
```

Total processing latency typically remains within **microseconds**.

---

# 16. Summary

AMPS combines:

* real-time pub/sub messaging
* in-memory state management
* efficient filter-based routing
* delta messaging
* durable logging
* fast failure recovery

to create a messaging platform capable of handling **hundreds of thousands to millions of events per second with extremely low latency**.

This architecture makes AMPS ideal for:

* financial trading systems
* real-time analytics
* distributed state synchronization
* telemetry pipelines
* high-frequency event processing
