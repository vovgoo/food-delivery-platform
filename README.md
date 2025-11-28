# 🚀 Food Delivery Platform

Modular microservice ecosystem delivering the full food-ordering journey: user onboarding, restaurant catalog, order lifecycle, payments, and omnichannel notifications. Built with **Spring Boot 3.4**, **Spring Cloud 2024.x**, **Kafka**, **RabbitMQ**, **Redis**, and **PostgreSQL**, orchestrated via **Eureka** (discovery) and **Config Server** (centralized config). All external traffic hits the **API Gateway**, while asynchronous flows travel through Kafka and RabbitMQ. Infrastructure for local development spins up with Docker Compose.

---

## 🗺️ Repository Map

| Path | Description |
| --- | --- |
| `backend/api-gateway` | Spring Cloud Gateway: JWT validation, header relay, Redis rate limiting, Swagger aggregation. |
| `backend/user-service` | Identity, JWT issuing, OTP flows, address book, RabbitMQ events, JWKS endpoint. |
| `backend/restaurant-service` | Restaurant/dish CRUD, Imgbb image uploads, internal data for order validation. |
| `backend/order-service` | Order & payment lifecycle, Feign validation with other services, Kafka event streaming. |
| `backend/notification-service` | Kafka/Rabbit listeners sending email + SMS (Mailhog placeholder). |
| `backend/config-server` | Spring Cloud Config (native profile) serving YAML from `config-repo/`. |
| `backend/eureka-server` | Service registry UI + discovery. |
| `backend/common-*` | Additional shared modules (client/config/domain/event/security) present but not wired into `settings.gradle`. |
| `config-repo` | Configuration store per service (`*-dev.yaml`, `*-prod.yaml`) with DB/broker/JWT settings. |
| `frontend` | Placeholder directory for React UI. |
| `docker-compose.yml` | Local infra stack: Postgres (per service), Redis, Kafka/ZooKeeper, RabbitMQ, Mailhog, service containers. |

---

## 🏛️ Architecture Overview

### Ingress & Routing
- **API Gateway** runs WebFlux OAuth2 resource server: JWKS fetched from user-service, JWT validated, `X-User-Id` & `X-Roles` headers injected, Redis rate limits enforced per route, Swagger UI aggregates downstream OpenAPI specs.

### Discovery & Configuration
- **Eureka** registers every Spring service.
- **Config Server** runs in `native` mode pointing at `config-repo/`, distributing JDBC URLs, credentials, RSA keys, Redis/Kafka/Rabbit settings, and internal tokens.

### Storage & Messaging
- **PostgreSQL** per domain service (Liquibase changelog in each module).
- **Redis** for API Gateway throttling (`redis-api-gateway`) and user-service OTP/refresh limits (`redis-user`).
- **RabbitMQ** (`user.events` topic) for OTP/sign-up/email-change flows.
- **Kafka** for order domain events (`order.created`, `order.status.changed`) consumed by notification-service.

### Security Backbone
- Shared filters from `backend/common`:
  - `HeaderAuthenticationFilter` checks `X-User-Id`/`X-Roles` against user-service before authenticating.
  - `InternalHeaderAuthenticationFilter` validates `/internal/**` access via `X-Service-Name` + `X-Internal-Token`.
- Per-service `SecurityConfig` defines separate `dev` (Swagger allowed) and `prod` (restricted) chains.

---

## 🔍 Service Deep Dive

| Service | Core Duties | Data & Infra | Sync Dependencies | Async Dependencies | Security Highlights | Notes |
| --- | --- | --- | --- | --- | --- | --- |
| **API Gateway** | JWT verification, header relay, rate limiting, Swagger aggregation | Redis (`redis-api-gateway`), Config Server | Config Server, Eureka, user-service (JWKS) | — | WebFlux `SecurityWebFilterChain`, `JwtHeaderRelayFilter`, per-route `RedisRateLimiter` | Needs `X-Forwarded-For` support; rate configs hard-coded |
| **User Service** | Sign-up/login/refresh, profile & address book, OTP via Redis/Rabbit, JWKS endpoint | PostgreSQL (`userdb`), Redis (`redis-user`), RabbitMQ | Config Server, Eureka | Rabbit queues (`signup.phone`, `phone.change`, `email.change`) | Dual dev/prod security chains, RSA JWT issuing, shared header filters | RSA keys/internal tokens stored in repo, no in-service throttling, shared Redis namespace |
| **Restaurant Service** | Manage restaurants/dishes/images, expose internal endpoints for order validation | PostgreSQL (`restaurantdb`), Imgbb API | Config Server, Eureka | — | HTTP ACL for `ADMIN`/`INTERNAL`, header filters | Imgbb key in config, no fallback; limited domain-level authorization |
| **Order Service** | Orchestrate order lifecycle, payment strategies, Feign validation, Kafka events | PostgreSQL (`orderdb`), Kafka | Config Server, Eureka, user/restaurant services | Kafka topics (`order.created`, `order.status.changed`) | Header/internal filters, RBAC by HTTP method | Internal token duplication, no outbox/idempotency, no circuit breaker |
| **Notification Service** | Listen to Rabbit/Kafka and send email/SMS | Kafka, RabbitMQ, SMTP (Mailhog) | Config Server, Eureka | Kafka + Rabbit | No HTTP surface; listener-only | SMS stub via email, no DLQ/retry |
| **Config Server** | Serve YAML configs | Local FS (`config-repo`) | — | — | No auth | Exposes secrets if internet-facing |
| **Eureka Server** | Discovery | In-memory | — | — | UI unauthenticated | Needs TLS + restricted access |
| **Common Module** | DTOs, enums, filters, validators | — | Imported by services | — | Implements shared security filters | Duplicate `common-*` modules need cleanup |

---

## 📦 Repository Structure Highlights
1. **`backend/`** — Gradle multi-module project; each service has `build.gradle`, Dockerfile, and Liquibase changelog.
2. **`config-repo/`** — Configuration source for Config Server; contains sensitive data (DB creds, RSA keys, internal tokens). Keep private!
3. **`docker-compose.yml`** — Brings up infrastructure plus optional service containers; expects `.env` for secrets.
4. **`frontend/`** — Empty placeholder for future UI work.
5. **`build/` directories** — Gradle outputs; can be ignored in docs.

---

## 🛠️ Local Development Guide

1. **Requirements:** JDK 21, Docker & Compose v2, Node.js (future frontend).
2. **Build shared artifacts:**
   ```bash
   ./gradlew :backend:common:build
   ./gradlew clean build
   ```
3. **Configure `.env`:** copy variables referenced in `docker-compose.yml` (DB names, credentials, JWT secrets, ports).
4. **Start infrastructure:**
   ```bash
   docker compose up config-server eureka-server mailhog rabbitmq \
     redis-user redis-api-gateway postgres-user postgres-restaurant postgres-order \
     zookeeper kafka
   ```
5. **Run services:**
   - Docker: `docker compose up user-service restaurant-service order-service notification-service api-gateway`
   - Gradle: `./gradlew :backend:<service>:bootRun --args='--spring.profiles.active=dev'`
6. **Entry points:**
   - Config Server → `http://localhost:8888/{application}/{profile}`
   - Eureka → `http://localhost:8761`
   - Mailhog → `http://localhost:8025`
   - API Gateway → `http://localhost:8080/api/...`
   - Swagger Hub → `http://localhost:8080/swagger-ui.html`
7. **Tests:** `./gradlew test` (per service or root). Add Testcontainers (Postgres/Kafka/Rabbit) for integration coverage.

---

## 🔐 Security & Observability Notes

- **Secrets:** RSA keys, DB creds, internal tokens currently stored in repo; move to Vault/Secret Manager and cut Config Server public access.
- **TLS:** All services run over HTTP—front them with TLS ingress and consider mTLS for inter-service calls.
- **Rate Limiting:** Gateway-only throttling; add per-service safeguards (especially user-service OTP routes).
- **Messaging Resilience:** Rabbit/Kafka consumers lack DLQ/retry/backoff—configure dedicated queues/topics plus monitoring for consumer lag.
- **Observability:** Actuator enabled; hook into Prometheus/OpenTelemetry and centralized logging to track errors/latency.
- **Testing Debt:** Minimal automated tests; introduce Testcontainers suites, contract tests for Feign clients, and end-to-end flows.

---

## 📌 Next Steps

1. Secure secrets (RSA, DB, tokens) via secret management.
2. Add Resilience4j (retry/circuit-breaker) for Feign clients; implement Kafka outbox or transactional producer to ensure delivery.
3. Configure DLQ/retry for Rabbit/Kafka and monitor consumer health.
4. Support `X-Forwarded-For` in gateway rate limiter and externalize throttle configs in Config Server.
5. Expand automated testing (OTP flows, Imgbb integration, order/payment scenarios).
6. Kick off real frontend leveraging aggregated OpenAPI specs.

---

## 📚 Service-Level Docs

- [`backend/user-service/README.md`](backend/user-service/README.md)
- [`backend/order-service/README.md`](backend/order-service/README.md)
- [`backend/restaurant-service/README.md`](backend/restaurant-service/README.md)
- [`backend/notification-service/README.md`](backend/notification-service/README.md)

Each file describes responsibilities, architecture, configuration, local setup, testing, and TODOs for its service.

---

## 🤝 Contributing

1. Fork & branch (`feature/<name>`).  
2. Run `./gradlew clean build` (plus specific service tests).  
3. Update documentation when behavior changes.  
4. Open PR with details + impact on other services.  

Keep this README up to date—it's the gateway for new contributors and reviewers 🙌


