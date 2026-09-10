# Fuel Transaction Processor

Reactive microservice that ingests simulated fuel-card purchase webhooks, processes them asynchronously over JMS, stores transactions in PostgreSQL, and serves query/analytics APIs with Redis-backed driver spend summaries — the same patterns used in logistics and fintech payment webhook systems.

## Architecture

```
[External Fuel Card Provider]
        |
        v (webhook POST)
[WebhookController] --> validates signature/payload
        |
        v (publishes event)
[JMS Queue: fuel-transaction-events]
        |
        v (consumer)
[TransactionProcessorService] --> idempotency + fraud rules
        |
        v
[PostgreSQL: fuel_transactions]
        |
        v (cache refresh)
[Redis: driver spend summary]

[REST API] --> transactions, review, summaries, CSV export
[WebSocket /ws/transactions] --> live processed events
```

## Tech stack

- Java 17, Spring Boot 3.4, WebFlux
- ActiveMQ Artemis (JMS), PostgreSQL (R2DBC + Liquibase JDBC), Redis
- Resilience4j rate limiting, springdoc-openapi, Docker Compose

## Run locally

```bash
docker compose up --build
```

Swagger UI: http://localhost:8080/swagger-ui.html  
Health: http://localhost:8080/actuator/health

### Sample webhook curl

```bash
BODY='{"externalTransactionId":"TXN-88213-2026","driverId":"DRV-4471","vehicleId":"VEH-1029","cardNumberMasked":"****4417","merchantName":"Pilot Travel Center","merchantLocation":"Amarillo, TX","gallons":92.5,"pricePerGallon":3.42,"totalAmount":316.35,"transactionTimestamp":"2026-09-10T14:32:00Z"}'

SIG=$(printf '%s' "$BODY" | openssl dgst -sha256 -hmac 'fueltx-webhook-secret' | awk '{print $2}')

curl -i -X POST http://localhost:8080/api/v1/webhooks/fuel-transactions \
  -H "Content-Type: application/json" \
  -H "X-Webhook-Signature: $SIG" \
  -d "$BODY"
```

### Tests

```bash
./mvnw test
```

## Design decisions

- **Async via JMS instead of direct DB write:** The webhook returns `202 Accepted` immediately so provider retries are not blocked by slow persistence. Ingestion and processing are decoupled, matching real payment webhook patterns.
- **Redis for driver summaries:** Aggregates are read often; caching week/month spend reduces repeated scans of `fuel_transactions`, with TTL as an eventual-consistency safety net.
- **Idempotency on `externalTransactionId`:** Providers redeliver webhooks. Duplicate deliveries must not create double charges or double-counted spend.
- **Fraud rules as Strategy:** Each rule (`GallonsThreshold`, `RapidFill`) is a separate bean composed by `FraudCheckService`, so new rules can be added without changing the processor.

## Stretch features included

- CSV streaming export: `GET /api/v1/transactions/export`
- Webhook rate limiting (Resilience4j): 100 req/min
- Live WebSocket feed: `ws://localhost:8080/ws/transactions`
