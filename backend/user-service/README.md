# 👤 User Service

> Core identity & profile hub for the food-delivery platform. Issues JWTs, manages OTP flows, and exposes internal APIs for the rest of the stack.

## ✨ Highlights

- 🔐 RSA-backed JWT issuance with JWKS endpoint for API Gateway.
- 📨 OTP workflows powered by Redis + RabbitMQ queues.
- 📬 Rich address book + profile management with internal exposure for other services.

## 🗂️ Responsibilities

| Area | What happens |
| --- | --- |
| Identity | Email/phone sign-up, password sign-in, refresh tokens, JWKS exposure. |
| Profile | CRUD for personal data, password/email/phone change with OTP. |
| Addresses | CRUD + lookup for `/internal/addresses/{userId}/{addressId}`. |
| Verification | Redis-backed OTP codes, attempts counters, throttling. |
| Messaging | Publishes sign-up/phone-change/email-change events to RabbitMQ. |

## 🧱 Architecture

- **Controllers (`controller/`)**: split into auth, user, address, internal, JWKS.
- **Services (`service/`)**: domain-specific logic + dedicated subpackages for redis, rabbit, security.
- **Persistence (`entity/`, `repository/`)**: Spring Data JPA with Liquibase migrations.
- **DTOs/Mappers** keep transport models decoupled from entities.
- **SecurityConfig** defines separate `dev`/`prod` chains and injects shared header filters.
- **Redis key objects** live under `domain.redis.*` so TTL/namespace are centralized.

## 🗃️ Data & Infrastructure

| Component | Description |
| --- | --- |
| 🐘 PostgreSQL (`userdb`) | Users, roles, addresses. Migrations in `db/changelog`. |
| 🧠 Redis (`redis-user`) | OTP codes, attempt counters, refresh tokens, address limits. |
| 🐇 RabbitMQ | Topic exchange `user.events` with queues for OTP/verification events. |
| ⚙️ Config Server | Loads `config-repo/user-service-{profile}.yaml` at boot. |

## 📬 Messaging Contracts

| Exchange / Queue | Payload | Purpose |
| --- | --- | --- |
| `user.events` → `signup.phone.queue` | `SignUpPhoneEvent` | Sign-up OTP SMS |
| `user.events` → `phone.change.queue` | `PhoneChangeEvent` | Phone-change OTP |
| `user.events` → `email.change.queue` | `EmailChangeEvent` | Email-change confirmation link |

## 🔐 Security Highlights

- RSA pair via `app.jwt.keys.*`, JWKS exposed at `/.well-known/jwks.json`.
- `HeaderAuthenticationFilter` validates `X-User-Id`/`X-Roles` by querying user status before authenticating.
- `InternalHeaderAuthenticationFilter` secures `/internal/**` endpoints via `X-Service-Name` + `X-Internal-Token`.
- Distinct `SecurityFilterChain` beans for `dev` (Swagger allowed) and `prod` (minimum exposure).

## 🏃 Running Locally

1. **Spin up infra:** `docker compose up postgres-user redis-user rabbitmq`.
2. **Start config stack:** run Config Server + Eureka (`./gradlew :backend:config-server:bootRun`, etc.).
3. **Launch service:**  
   `./gradlew :backend:user-service:bootRun --args='--spring.profiles.active=dev'`
4. **Hit endpoints:**  
   - Auth: `/api/v1/auth/signUp`, `/signIn`, `/refresh`  
   - Profile: `/api/v1/users/me`, `/api/v1/address/**`  
   - JWKS: `/.well-known/jwks.json`  
   - Internal: `/internal/users/{id}`, `/internal/addresses/{userId}/{addressId}`

## 🧪 Testing

- `./gradlew :backend:user-service:test`
- Recommended: add Testcontainers suites for Postgres/Redis/RabbitMQ, plus security regression tests for header filters + OTP throttling.

## 📈 Observability & TODOs

- Actuator `/actuator/health`, `/actuator/info` already exposed—wire them to Prometheus/OpenTelemetry for prod.
- Move RSA keys/internal tokens to a secret manager.
- Add in-service rate limiting (current throttling only at API Gateway).
- Expand integration tests for verification + address limits.



