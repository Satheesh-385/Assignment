# AI Social Media Backend Assignment

## Tech Stack

- Java 21
- Spring Boot 4
- PostgreSQL
- Redis
- Docker
- JPA / Hibernate

---

# Features Implemented

## Phase 1 - Core API & Database Setup

### Entities

- User
- Bot
- Post
- Comment

### REST APIs

- POST /api/posts
- POST /api/posts/{postId}/comments
- POST /api/posts/{postId}/like
- POST /api/bot/reply/{postId}

---

# Phase 2 - Redis Virality Engine & Atomic Locks

## Virality Score

Virality scores are stored in Redis.

### Rules

- Bot Reply = +1
- Human Like = +20
- Human Comment = +50

### Redis Key Example

```text
post:{id}:virality_score




Atomic Locks
Horizontal Cap
Maximum 100 bot replies per post
Redis atomic INCR operation used
Redis Key
post:{id}:bot_count
Rejects request with HTTP 429 when limit exceeds 100
Vertical Cap
Maximum comment depth = 20
Request rejected if depth level exceeds 20
Cooldown Cap
Same bot cannot interact with same human within 10 minutes
Redis Key
cooldown:bot_{id}:human_{id}
Redis TTL used for automatic expiration
Phase 3 - Notification Engine
Smart Notification Batching
Notification Throttling

If user already received a notification within 15 minutes:

Notification stored in Redis List
Redis Key
user:{id}:pending_notifs

Otherwise:

Push notification sent immediately
Notification cooldown key created
CRON Sweeper

A scheduled task runs every 5 minutes.

Responsibilities
Scans pending notifications
Aggregates notifications
Sends summarized notification
Example Output
Summarized Push Notification:
Bot X and 3 others interacted with your posts.
Thread Safety & Concurrency Handling

Redis acts as the gatekeeper for concurrency-sensitive operations.

How Thread Safety Was Guaranteed
Horizontal Bot Cap

Used Redis atomic INCR operation:

Long count = redisTemplate.opsForValue().increment(key);

This guarantees:

No race conditions
Exact cap enforcement under concurrent requests
Prevents database from exceeding 100 bot replies
Transaction Safety

Bot reply creation uses:

@Transactional

This ensures:

Database commits only after Redis checks pass
PostgreSQL remains source of truth
Redis safely controls access
Stateless Architecture

Application is fully stateless.

No in-memory storage used:

No HashMap
No static variables

All runtime state stored in Redis:

Virality scores
Bot counters
Cooldowns
Pending notifications
Running the Project
Start PostgreSQL & Redis
docker compose up -d
Run Spring Boot Application
mvn spring-boot:run
Docker Services
PostgreSQL
Port: 5432
Redis
Port: 6379
Postman Collection

Included in repository:

postman_collection.json


GitHub Repository

Repository Link:

https://github.com/Satheesh-385/Assignment