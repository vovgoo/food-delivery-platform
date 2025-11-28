# 📦 Order Service

> Orchestrates the entire order lifecycle: validation, persistence, payments, and event streaming.

## 💡 Responsibilities

| Domain | Capabilities |
| --- | --- |
| Orders | Create/list/detail orders, handle status transitions with RBAC. |
| Payments | Strategy handlers for card, wallets, bank transfer, COD, etc. |
| Validation | Double-check addresses (user-service) and menu items (restaurant-service). |
| Messaging | Publish `OrderCreatedEvent` & `OrderStatusChangedEvent` to Kafka. |

## 🧱 Architecture

- **REST layer:** `OrderController` serves `/api/v1/orders`.
- **Service layer:** `OrderServiceImpl` + `OrderFacade` coordinate repositories, clients, and payment strategies.
- **Feign clients:** `InternalAddressClient`, `InternalRestaurantClient` fetch canonical data through Eureka discovery + internal headers.
- **Payment strategy:** `service.payment` package hosts factory + handler classes per payment method.
- **Kafka module:** `KafkaEventPublisher` enforces payload type via typed descriptors in `domain.kafka.key`.
- **Security:** Shared header filters authenticate both user-facing and internal requests; HTTP rules defined in `SecurityConfig`.

## 🗃️ Data & Infra

| Component | Role |
| --- | --- |
| 🐘 PostgreSQL (`orderdb`) | Stores orders, items, payments (Liquibase migrations). |
| 📡 Kafka | Topics `order.created`, `order.status.changed` (auto-created on boot). |
| ⚙️ Config/Eureka | Supplies config + discovery for Feign and Kafka. |

## 🔗 Integrations

- **User Service:** `/internal/addresses/{userId}/{addressId}` ensures address ownership regardless of status.
- **Restaurant Service:** `/internal/restaurants/**` provides menu + availability validation.
- **Notification Service:** Listens to Kafka topics to send SMS/email notifications downstream.

## 🔐 Security Model

- `HeaderAuthenticationFilter` trusts gateway headers and blocks blocked/deactivated users.
- `InternalHeaderAuthenticationFilter` protects `/internal/**` via service-to-service tokens.
- HTTP rules:  
  - POST `/orders` → `ROLE_USER`  
  - GET `/orders/**` → `ROLE_USER` or `ROLE_ADMIN`  
  - PUT `/orders/*/status` → `ROLE_ADMIN`
- Separate `dev` (Swagger enabled) and `prod` filter chains.

## 🏃 Run It Locally

1. Bring up infra: `docker compose up postgres-order kafka zookeeper`.
2. Start Config Server + Eureka.
3. (Optional) build shared module: `./gradlew :backend:common:build`.
4. Launch service:  
   `./gradlew :backend:order-service:bootRun --args='--spring.profiles.active=dev'`
5. Default port: `8083` (override via Config Server or `SERVER_PORT` env).

## 🧪 Testing & Tooling

- `./gradlew :backend:order-service:test`
- Suggested: Testcontainers for Postgres/Kafka, contract tests for Feign clients, idempotency regression tests.

## 📈 Observability & TODOs

- Exposes `/actuator/health` & `/info`; add Prometheus/OpenTelemetry exporters.
- Add resilience (Retry/CircuitBreaker) + outbox or transactional producer for Kafka guarantees.
- Move internal service tokens & Kafka creds to a secrets manager.



