# 🧠 System Design Interview — Complete Flow Guide

> A structured, step-by-step playbook for tackling any system design interview with confidence.

**Time budget for a 45-min interview:**
| Step | Time |
|---|---|
| Functional Requirements | 3–5 min |
| Non-Functional Requirements | 5 min |
| Entities / Data Model | 5 min |
| API Design | 5 min |
| High-Level Design | 10–15 min |
| Deep Dives | 10 min |

---

## 📋 Table of Contents
1. [Functional Requirements](#1-functional-requirements)
2. [Non-Functional Requirements](#2-non-functional-requirements)
3. [Entities & Data Model](#3-entities--data-model)
4. [API Design](#4-api-design)
5. [High-Level Design (HLD)](#5-high-level-design-hld)
6. [Deep Dives](#6-deep-dives)
7. [Communication Protocols](#7-communication-protocols)

---

## 1. Functional Requirements

> **Goal:** Define the exact scope. What does the system *do*? Be ruthless about what's in vs out of scope.

### What to do:
- Ask: *"What are the core features we need to support?"*
- Separate **must-have** (MVP) from **nice-to-have** (stretch goals)
- Ask: *"Should I focus on any specific feature or user flow?"*
- Restate requirements back to the interviewer — confirm alignment before proceeding

### Template:
```
Core Features (in scope):
  - Users can do X
  - System supports Y
  - Z happens when...

Out of scope (explicitly state):
  - No support for A
  - Not handling B in this design
```

### Example (designing Twitter/X):
```
In Scope:
  - Post a tweet (≤280 chars)
  - Follow/unfollow users
  - View a home timeline (feed of followed users)
  - Like a tweet

Out of Scope:
  - DMs, Ads, Trending topics, Video upload
```

---

## 2. Non-Functional Requirements

> **Goal:** Put concrete numbers to the system's quality attributes. This drives every architectural decision that follows.

### Questions to ask the interviewer:

| Dimension | Questions to ask |
|---|---|
| **Scale** | How many daily/monthly active users? Read-heavy or write-heavy? |
| **Latency** | What's acceptable response time for the core feature? |
| **Availability** | What's the uptime requirement? Can the system tolerate brief downtime? |
| **Consistency** | Is it okay to show slightly stale data? Or must reads reflect latest writes? |
| **Durability** | Can we afford to lose any data? (e.g., logs vs. payments) |
| **Geography** | Single region or global? |

---

### How to put numbers to each dimension:

#### 📈 Scale — Estimations
Work top-down from DAU (Daily Active Users):

```
Example: 100M DAU, each user reads feed 10x/day, posts 1x/day

Reads:  100M × 10 = 1B reads/day  → ~12,000 reads/sec  → ~36,000 reads/sec peak (3x)
Writes: 100M × 1  = 100M writes/day → ~1,200 writes/sec → ~3,600 writes/sec peak

Storage: 1 tweet = ~300 bytes
         100M writes/day × 300B = 30 GB/day → ~11 TB/year
```

**Rules of thumb:**
| Metric | Value |
|---|---|
| 1M requests/day | ~12 req/sec |
| 1B requests/day | ~12,000 req/sec |
| Peak multiplier | 2x–3x avg |
| 1 char | 1 byte |
| Average row (metadata) | 100–500 bytes |
| Image | 200 KB–2 MB |
| Video (1 min, compressed) | ~50–100 MB |

---

#### ⏱️ Latency — Putting a Number
Ask the interviewer, then propose if they don't specify:

| User expectation | Target p99 latency |
|---|---|
| Real-time feel (chat, gaming) | < 100ms |
| Interactive UI (feed, search) | < 300ms |
| Background / async (reports) | < 2s or best-effort |

**Latency cheat sheet** (numbers every engineer should know):
| Operation | Latency |
|---|---|
| L1 cache hit | ~1 ns |
| RAM access | ~100 ns |
| SSD read | ~100 µs |
| Network (same DC) | ~500 µs |
| Network round trip (cross-region) | ~150 ms |
| HDD read | ~10 ms |
| Redis GET | ~0.5–1 ms |
| DB query (indexed) | ~1–10 ms |

---

#### 🔁 Consistency — Which model to pick

| Model | Meaning | Use when |
|---|---|---|
| **Strong** | Reads always reflect latest write | Payments, inventory, auth |
| **Eventual** | Reads may lag slightly behind writes | Social feeds, likes, view counts |
| **Read-your-writes** | You always see your own writes | User profile updates |
| **Causal** | Related ops appear in order | Comments, threaded replies |

> 💡 **Interview move:** State your choice explicitly — *"I'll use eventual consistency for the feed since slight staleness is acceptable, but strong consistency for the payment service."*

---

#### 🟢 Availability — SLAs and what they mean

| SLA | Downtime/year | Downtime/month |
|---|---|---|
| 99% | 87.6 hrs | 7.3 hrs |
| 99.9% (three nines) | 8.7 hrs | 43.8 min |
| 99.99% (four nines) | 52 min | 4.4 min |
| 99.999% (five nines) | 5 min | 26 sec |

> Most web services target **99.9% – 99.99%**. Five nines requires serious investment (active-active multi-region, chaos engineering).

---

### NFR Summary Template:
```
- Scale:        ~X DAU, Y reads/sec, Z writes/sec (peak)
- Latency:      p99 < Xms for [core feature]
- Availability: 99.99% uptime (< 1hr downtime/year)
- Consistency:  Eventual for [feed/social]; Strong for [payments/auth]
- Durability:   No data loss for [user data]; logs can be lossy
- Geography:    Single region for now, design for multi-region later
```

---

## 3. Entities & Data Model

> **Goal:** Define the nouns of your system. What are the core objects, their fields, and relationships?

### What to do:
- List core entities (usually 3–6 for most systems)
- Define key fields and their types
- Identify relationships (1:1, 1:many, many:many)
- Think about what gets queried together → influences DB choice and schema

### Template:
```
Entity: User
  - id:         UUID (PK)
  - username:   String
  - email:      String
  - created_at: Timestamp

Entity: Tweet
  - id:         UUID (PK)
  - user_id:    UUID (FK → User)
  - content:    String (≤280 chars)
  - created_at: Timestamp

Relationship:
  - User follows User       → many:many → Follow table (follower_id, followee_id)
  - User likes Tweet        → many:many → Like table (user_id, tweet_id)
```

### DB selection signal:
| Signal | Choose |
|---|---|
| Many joins, ACID needed | PostgreSQL / MySQL |
| Flexible schema, documents | MongoDB |
| Massive write throughput | Cassandra |
| Key-value, caching | Redis |
| Global scale, managed | DynamoDB |

---

## 4. API Design

> **Goal:** Define the contract between client and server. What endpoints/methods does the system expose?

### What to do:
- Cover the core user-facing operations (CRUD + key flows)
- Use REST for most cases (or gRPC if low-latency internal services)
- Include: method, path, key request params, key response fields
- Mention auth (JWT / API key) and pagination strategy

### REST API Template:
```
POST   /v1/tweets
  Body: { content: string }
  Returns: { tweet_id, created_at }

GET    /v1/feed
  Params: ?cursor=<last_tweet_id>&limit=20
  Returns: { tweets: [...], next_cursor }

POST   /v1/users/{user_id}/follow
  Returns: 200 OK

DELETE /v1/users/{user_id}/follow
  Returns: 200 OK

GET    /v1/tweets/{tweet_id}
  Returns: { tweet_id, content, like_count, author }
```

### Pagination patterns:
| Pattern | Use when |
|---|---|
| **Cursor-based** | Feeds, infinite scroll (handles inserts well) |
| **Offset-based** | Admin panels, search results (simple but slow at scale) |
| **Keyset** | Time-series data sorted by ID or timestamp |

---

## 5. High-Level Design (HLD)

> **Goal:** Draw the system at 10,000 feet. Clients → Load Balancer → Services → Storage. Make it work correctly first, then make it scale.

### Core components to place:
```
[Client]
    ↓
[CDN] ← static assets, cached responses
    ↓
[Load Balancer] ← L7, routes by path/header
    ↓
[API Gateway] ← auth, rate limiting, routing
    ↓
[Services]
  ├── User Service
  ├── Tweet Service
  ├── Feed Service
  └── Notification Service
    ↓
[Message Queue] ← Kafka / SQS (async fan-out)
    ↓
[Storage Layer]
  ├── Primary DB (PostgreSQL / MySQL)
  ├── Cache (Redis)
  ├── Object Store (S3 — images, video)
  └── Search Index (Elasticsearch)
```

### HLD checklist:
- [ ] Client to server communication defined
- [ ] Load balancing in place
- [ ] Auth handled (API Gateway or middleware)
- [ ] Core happy path traced end-to-end
- [ ] Storage layer chosen and justified
- [ ] Async flows identified (what doesn't need to be synchronous?)
- [ ] Where does caching help?

---

## 6. Deep Dives

> **Goal:** Show senior-level thinking. Pick 2–3 areas and go deep. Let the interviewer guide you, or propose the trickiest parts yourself.

### Common deep dive topics:

---

#### 🔀 Horizontal Scaling
- **Stateless services** → can scale horizontally freely (add more instances behind LB)
- **Stateful services** → need session affinity or externalize state (move sessions to Redis)
- **DB scaling:**

| Strategy | How | Trade-off |
|---|---|---|
| Read replicas | Route SELECTs to replicas | Replication lag (eventual consistency) |
| Sharding | Partition data by shard key (user_id % N) | Cross-shard queries are hard |
| Vertical scaling | Bigger machine | Has a ceiling, expensive |
| CQRS | Separate read/write models | Complexity, eventual consistency |

---

#### ⚡ Redis Caching
- **What to cache:** hot reads, computed aggregates, session tokens, rate limit counters
- **Cache patterns:**

| Pattern | How it works | Use when |
|---|---|---|
| **Cache-aside** | App checks cache → miss → reads DB → populates cache | General reads |
| **Write-through** | Write to cache AND DB together | Low write volume, high read consistency |
| **Write-behind** | Write to cache → async flush to DB | High write throughput, can tolerate lag |
| **Read-through** | Cache fetches from DB on miss automatically | Simplified app logic |

- **Eviction policies:** LRU (most common), LFU, TTL-based
- **Cache invalidation:** hardest problem — use TTL + event-driven invalidation via Kafka

```
Cache key design:
  feed:{user_id}           → list of tweet IDs
  tweet:{tweet_id}         → tweet data
  rate_limit:{user_id}     → request count (with TTL)
  session:{session_token}  → user session
```

---

#### 📨 Kafka (Async Messaging & Fan-out)
- **Why Kafka:** decouple producers from consumers, absorb traffic spikes, replay events
- **Key concepts:**

| Concept | Meaning |
|---|---|
| **Topic** | Named stream of events (e.g., `tweet.created`) |
| **Partition** | Parallelism unit within a topic; ordered within partition |
| **Consumer Group** | Multiple consumers share load; each partition → 1 consumer |
| **Offset** | Position in a partition; commit after processing |
| **Retention** | Messages stored for X days (can replay) |

- **Fan-out pattern (e.g., Twitter feed):**
```
User posts tweet
  → Tweet Service publishes to Kafka topic: tweet.created
     → Feed Service consumes → pushes tweet to followers' feed cache
     → Notification Service consumes → sends push notifications
     → Search Indexer consumes → indexes tweet for search
     → Analytics Service consumes → updates metrics
```

- **Push vs Pull fan-out:**

| Model | How | Use when |
|---|---|---|
| **Push (fan-out on write)** | Pre-compute feed at write time | Users with few followers; low-latency reads |
| **Pull (fan-out on read)** | Merge feeds at read time | Celebrities (millions of followers) |
| **Hybrid** | Push for normal users, pull for celebrities | Twitter's actual approach |

---

#### 🔍 Other Common Deep Dives

**Rate Limiting:**
```
Token bucket algorithm in Redis:
  - Each user gets N tokens/second
  - Each request consumes 1 token
  - Tokens refill at fixed rate
  Key: rate_limit:{user_id} → {tokens, last_refill_time}
```

**CDN & Static Assets:**
- Push static files (JS, CSS, images) to CDN edge nodes
- Cache API responses at CDN for public/anonymous reads
- Use cache-control headers + cache invalidation on deploy

**Search (Elasticsearch):**
- Sync data from primary DB → Elasticsearch via Kafka consumer
- Full-text search, faceted filtering, fuzzy matching
- Not a primary store — always have source of truth in primary DB

**Geo-distribution:**
- Active-passive: one region serves traffic, other is hot standby
- Active-active: all regions serve traffic, harder consistency story
- Route users to nearest region via DNS (GeoDNS) or Anycast

---

## 7. Communication Protocols

> **Goal:** Choose the right protocol for the right use case. This often comes up in deep dives.

---

### REST (HTTP/1.1 or HTTP/2)
- **What:** Request-response over HTTP, stateless, resource-oriented
- **Format:** JSON (most common), XML
- **Use when:** Public APIs, CRUD operations, browser clients, simplicity matters
- **Trade-offs:** Higher overhead per request, half-duplex, no native streaming

```
GET  /v1/users/123       → fetch user
POST /v1/tweets          → create tweet
PUT  /v1/tweets/456      → update tweet
DEL  /v1/tweets/456      → delete tweet
```

---

### gRPC (HTTP/2 + Protobuf)
- **What:** High-performance RPC framework by Google; uses binary Protobuf serialization
- **Format:** Protocol Buffers (binary — ~5x smaller than JSON)
- **Use when:** Internal microservice-to-microservice calls, low-latency, streaming needed
- **Trade-offs:** Not human-readable, harder to debug, not browser-native

```
service TweetService {
  rpc CreateTweet (CreateTweetRequest) returns (Tweet);
  rpc StreamFeed  (FeedRequest) returns (stream Tweet);  // server streaming
}
```

| vs REST | gRPC wins |
|---|---|
| Latency | ✅ ~5–10x faster (binary + HTTP/2 multiplexing) |
| Streaming | ✅ Native bi-directional streaming |
| Browser support | ❌ Needs gRPC-web proxy |
| Human readability | ❌ Binary format |

---

### WebSockets (WS)
- **What:** Full-duplex, persistent TCP connection; both sides can send at any time
- **Use when:** Real-time two-way communication — chat, collaborative editing, live gaming, live trading
- **Trade-offs:** Stateful (harder to scale horizontally — need sticky sessions or pub/sub backend)

```
Flow:
Client → HTTP Upgrade request → Server accepts → WS connection open
Client ↔ Server: send frames in both directions freely
Client/Server closes connection when done
```

**Scaling WebSockets:**
```
Client → LB (sticky sessions / consistent hash by user_id)
       → WS Server (stateless logic)
       → Redis Pub/Sub or Kafka (broadcast messages across WS server instances)
```

---

### Server-Sent Events (SSE)
- **What:** One-way streaming from server to client over HTTP; client subscribes, server pushes
- **Use when:** Live feeds, notifications, dashboards, activity streams — where only server sends data
- **Trade-offs:** One-directional only; client must reconnect on disconnect (browser does this automatically)

```
Client: GET /v1/feed/live
        Accept: text/event-stream

Server:
  data: {"tweet_id": "abc", "content": "Hello!"}\n\n
  data: {"tweet_id": "def", "content": "World!"}\n\n
  ...
```

**SSE vs WebSockets:**
| | SSE | WebSocket |
|---|---|---|
| Direction | Server → Client only | Bi-directional |
| Protocol | HTTP (works through proxies) | WS (separate protocol) |
| Auto-reconnect | ✅ Built-in | ❌ Manual |
| Use case | Live feeds, notifications | Chat, gaming, collaboration |

---

### Long Polling
- **What:** Client sends request → server holds it open until data is available → responds → client immediately re-polls
- **Use when:** Legacy systems, environments where WS/SSE aren't supported
- **Trade-offs:** Higher server resource usage, latency jitter, not truly real-time

```
Client → GET /poll  (server waits up to 30s)
Server → responds when event occurs (or timeout)
Client → immediately sends next GET /poll
```

---

### Protocol Selection Matrix

| Use case | Best protocol |
|---|---|
| Public CRUD API | REST |
| Internal microservices | gRPC |
| Chat / collaborative editing | WebSockets |
| Live notifications / activity feed | SSE |
| Legacy real-time support | Long Polling |
| File upload / download | REST (multipart) |
| Low-latency multiplayer | UDP / WebRTC |

---

## 🗒️ Interview Cheat Sheet — One-liners

```
"I'll start by clarifying requirements before diving into the design."
"Let me put some numbers to this — assuming 100M DAU..."
"For the feed, I'll use eventual consistency since slight staleness is fine."
"I'll use Redis to cache hot reads and reduce DB load."
"Fan-out via Kafka lets us decouple the tweet write from all downstream effects."
"We can scale the API servers horizontally since they're stateless."
"For real-time notifications I'd use WebSockets or SSE depending on directionality."
"Let me trace through the happy path end-to-end before we optimize."
```

---

*System design interview flow guide — covers requirements gathering, estimation, data modeling, API design, HLD, deep dives, and communication protocols.*
