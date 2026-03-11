# 🗄️ Database Selection Guide — System Design Interview Cheat Sheet

> Anchor your DB choice to **data structure**, **access patterns**, **scale**, and **consistency requirements**.

---

## 📋 Table of Contents
- [SQL vs NoSQL — When to Choose](#-sql-vs-nosql--when-to-choose)
- [Decision Framework](#-decision-framework)
- [Database Breakdown](#-database-breakdown)
  - [MySQL](#-mysql--the-workhorse-rdbms)
  - [PostgreSQL](#-postgresql--the-advanced-open-source-rdbms)
  - [DynamoDB](#-dynamodb--aws-managed-key-value--document-store)
  - [Cassandra](#-cassandra--wide-column-store-for-massive-writes)
  - [MongoDB](#-mongodb--document-store-with-rich-query-language)
  - [Redis](#-redis--in-memory-data-structure-store)
- [CAP Theorem Summary](#-cap-theorem-summary)
- [Interview Quick Tips](#-interview-quick-tips)

---

## ⚖️ SQL vs NoSQL — When to Choose

### ✅ Choose SQL when:
- 🔗 You have **complex relationships & JOINs** (e-commerce, ERP, banking)
- ⚖️ You need **[ACID transactions](https://www.geeksforgeeks.org/dbms/acid-properties-in-dbms)** (payments, bookings, inventory)
- 📋 Schema is **well-defined and stable**
- 📊 You need **reporting, analytics, BI dashboards**
- 🏛️ You have **regulatory/audit compliance** requirements (SOX, HIPAA)

### ❌ Avoid SQL when:
- Massive horizontal scaling is needed (sharding SQL is painful)
- Schema is unstructured or rapidly evolving
- Write-heavy workloads at massive scale (100k+ writes/sec)
- Data is hierarchical or graph-like

---

### ✅ Choose NoSQL when:
- **[BASE properties](https://www.geeksforgeeks.org/dbms/base-properties-in-dbms)**
- 📈 You need **horizontal scalability** at massive write/read scale
- 🧩 Schema is **flexible or dynamic** (user-generated content)
- ⚡ You need **ultra-low latency** (<1ms) access patterns
- 🗺️ Data is document, key-value, graph, or time-series
- 🌍 You need **global distribution & multi-region replication**

### ❌ Avoid NoSQL when:
- Complex multi-table transactions are required
- Ad-hoc query patterns can't be predicted upfront
- Strong consistency across many entities is needed
- Heavy relational reporting is required

---

## 🧭 Decision Framework

| Question | SQL | NoSQL |
|---|---|---|
| Is the schema stable? | ✅ Yes | ❌ No |
| Need multi-entity transactions? | ✅ Yes | ❌ No |
| Need 100k+ writes/sec? | ❌ Hard | ✅ Yes |
| Access patterns known upfront? | Either | ✅ Preferred |
| Need strong consistency? | ✅ Yes | Depends |
| Geo-distributed users? | ⚠️ Hard | ✅ Yes |

---

## 🔍 Database Breakdown

---

### 🐬 MySQL — The Workhorse RDBMS
**Type:** SQL &nbsp;|&nbsp; **CAP:** CA (strong consistency, limited partition tolerance)

> Best for: transactional apps, CMSs, e-commerce. Vertical scaling preferred.

| Feature | Description |
|---|---|
| **InnoDB Engine** | Row-level locking, ACID transactions, foreign keys |
| **Read Replicas** | Scale reads horizontally; primary handles writes |
| **Query Cache** | Caches result sets for identical SELECT queries |
| **Index Types** | B-Tree (default), Full-text, Spatial indexes |
| **Partitioning** | Range/Hash/List partitioning for large tables |

---

### 🐘 PostgreSQL — The Advanced Open-Source RDBMS
**Type:** SQL &nbsp;|&nbsp; **CAP:** CA (favors consistency)

> Best for: complex queries, geo/analytics, hybrid workloads. Scales well with read replicas + connection pooling (PgBouncer).

| Feature | Description |
|---|---|
| **MVCC** | Multi-Version Concurrency Control — readers never block writers |
| **Parallel Queries** | Uses multiple CPU cores for large analytical queries |
| **JSONB** | Binary JSON storage with GIN indexes — blurs SQL/NoSQL line |
| **Table Partitioning** | Declarative range/list/hash partitioning |
| **Advanced Indexes** | GIN, GiST, BRIN, partial indexes — very flexible |

---

### ⚡ DynamoDB — AWS Managed Key-Value & Document Store
**Type:** NoSQL &nbsp;|&nbsp; **CAP:** AP by default (eventual consistency), optional strong consistency per-read

> Best for: session stores, shopping carts, leaderboards, IoT. Access patterns must be known upfront.

| Feature | Description |
|---|---|
| **Single-digit ms latency** | Guaranteed performance at any scale via partition key design |
| **Auto-scaling** | Capacity units scale automatically with traffic |
| **DAX (Cache)** | In-memory cache layer: microsecond reads |
| **Global Tables** | Multi-region, multi-active replication |
| **Streams** | Change data capture for event-driven architectures |

---

### 💎 Cassandra — Wide-Column Store Built for Massive Writes
**Type:** NoSQL &nbsp;|&nbsp; **CAP:** AP (highly available, eventual consistency)

> Best for: time-series, event logs, IoT telemetry, write-heavy at billions of rows. Used by Netflix, Apple.

| Feature | Description |
|---|---|
| **Masterless Architecture** | No single point of failure; peer-to-peer ring |
| **Tunable Consistency** | Choose per-query: ONE, QUORUM, ALL (CAP trade-off) |
| **Write Optimization** | Writes go to memtable + commit log → extremely fast |
| **Data Partitioning** | Consistent hashing distributes data across nodes |
| **Compaction** | SSTables merged in background; optimizes read performance |

---

### 🍃 MongoDB — Document Store with Rich Query Language
**Type:** NoSQL &nbsp;|&nbsp; **CAP:** CP by default (primary reads); tunable with read preferences

> Best for: catalogs, CMS, user profiles, real-time apps. Watch out for unbounded document growth.

| Feature | Description |
|---|---|
| **Flexible Schema** | BSON documents — no migrations needed for new fields |
| **Aggregation Pipeline** | Powerful multi-stage transformations inside DB |
| **Atlas Search** | Built-in Lucene full-text search |
| **Sharding** | Horizontal scaling via shard keys across clusters |
| **Change Streams** | Real-time event streams on collection changes |

---

### 🚀 Redis — In-Memory Data Structure Store
**Type:** NoSQL &nbsp;|&nbsp; **CAP:** Depends on config — single node CP, cluster trades consistency for availability

> Best for: caching, sessions, rate limiting, leaderboards, pub/sub. **NOT a primary DB** — data loss risk on crash.

| Feature | Description |
|---|---|
| **Sub-millisecond latency** | All data in RAM — fastest possible reads/writes |
| **Rich Data Structures** | Strings, Lists, Sets, Sorted Sets, Hashes, HyperLogLog |
| **Pub/Sub + Streams** | Message queues and real-time event streaming |
| **TTL / Eviction** | Built-in expiry for caching; configurable eviction policies |
| **Redis Cluster** | Horizontal sharding + replication for HA |

---

## 🔺 CAP Theorem Summary

> A distributed system can only guarantee **2 of 3**: Consistency, Availability, Partition Tolerance.
> Since partition tolerance is a must in distributed systems, the real trade-off is **C vs A**.

| Database | CAP | Consistency | Availability |
|---|---|---|---|
| MySQL | CA | ✅ Strong | ✅ High (single node) |
| PostgreSQL | CA | ✅ Strong | ✅ High (single node) |
| DynamoDB | AP | ⚠️ Eventual (default) | ✅ Very High |
| Cassandra | AP | ⚠️ Tunable | ✅ Very High |
| MongoDB | CP | ✅ Strong (primary) | ⚠️ May reject writes |
| Redis | CP/AP | Depends on config | Depends on config |

---

## 💡 Interview Quick Tips

1. **Always lead with access patterns** — NoSQL requires knowing them upfront; SQL gives you ad-hoc flexibility.
2. **Mention CAP theorem** when discussing trade-offs — interviewers love it.
3. **Redis is almost never a primary DB** — bring it up as a caching/session layer on top of your main store.
4. **Cassandra vs DynamoDB** — Cassandra = write throughput + on-prem control; DynamoDB = managed ops at scale.
5. **PostgreSQL > MySQL** for complex workloads — MVCC, JSONB, and advanced indexes make it more versatile.
6. **MongoDB sweet spot** — flexible schemas and rich queries, but watch for large document anti-patterns.
7. **For most startup/mid-scale systems** — default to PostgreSQL, add Redis for caching, revisit when you hit scale problems.

---

*Reference for system design interviews — covers SQL vs NoSQL decision-making and performance characteristics of MySQL, PostgreSQL, DynamoDB, Cassandra, MongoDB, and Redis.*
