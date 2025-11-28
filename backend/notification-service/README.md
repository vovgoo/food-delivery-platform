# 📣 Notification Service

> Reactive fan-out service that listens to domain events and delivers OTPs, order confirmations, and status updates via email/SMS.

## 🔔 Responsibilities

| Source | Event | Action |
| --- | --- | --- |
| RabbitMQ (`user.events`) | `SignUpPhoneEvent`, `PhoneChangeEvent`, `EmailChangeEvent` | Send SMS OTPs or email confirmation links. |
| Kafka (`order.created`) | Order accepted | Send SMS confirmation with order number. |
| Kafka (`order.status.changed`) | Status transition | Send SMS describing the new status. |

## 🧱 Architecture

- **Listeners:**  
  - `UserEventListener` → RabbitMQ queues.  
  - `OrderEventListener` → Kafka topics (`notification-group`).
- **Senders:** `EmailSender` & `SmsSender` abstractions hide delivery mechanism (currently JavaMailSender for both; SMS is a Mailhog placeholder).
- **Config:** Rabbit bindings declared by user-service; this service only listens. Kafka consumer configuration stored in Config Server.
- **Frontend link builder:** `FrontendProperty` supplies base URL for email-change confirmation links.

## 📨 Messaging Contracts

| Channel | Topic/Queue | Payload |
| --- | --- | --- |
| Kafka | `order.created` | `OrderCreatedEvent` |
| Kafka | `order.status.changed` | `OrderStatusChangedEvent` |
| RabbitMQ | `signup.phone.queue` | `SignUpPhoneEvent` |
| RabbitMQ | `phone.change.queue` | `PhoneChangeEvent` |
| RabbitMQ | `email.change.queue` | `EmailChangeEvent` |

## 📮 Email & SMS Delivery

- **Email:** `EmailSenderImpl` creates MIME messages via JavaMailSender (Mailhog in dev). Sender defaults to `vovgoo@innowise.com`.
- **SMS:** `SmsSenderImpl` currently reuses JavaMailSender to emulate SMS delivery (Mailhog). Interface matches real provider needs, so swapping to Twilio/SNS/etc. is trivial.

## ⚙️ Configuration

Defined in `config-repo/notification-service-{profile}.yaml`:

- `spring.kafka.*` → brokers, consumer group, JSON trusted packages.
- `spring.rabbitmq.*` → host, port, credentials.
- `spring.mail.*` → SMTP settings (Mailhog defaults).
- `app.frontend.email-change-route` → base URL for verification links.

## 🏃 Running Locally

1. Start brokers & SMTP mock: `docker compose up kafka zookeeper rabbitmq mailhog`.
2. Run Config Server + Eureka.
3. Launch service:  
   `./gradlew :backend:notification-service:bootRun --args='--spring.profiles.active=dev'`
4. Service listens on `8084` (no public REST APIs; Actuator available).
5. Inspect outgoing messages via Mailhog `http://localhost:8025`.

## 🧪 Testing & Observability

- `./gradlew :backend:notification-service:test`
- Add Testcontainers-based integration tests for Kafka + Rabbit listeners.
- Actuator health/info enabled—extend with metrics for consumer lag and listener failures.

## 🚀 Future Improvements

- Replace SMS email stub with a real gateway + retry/backoff logic.
- Configure DLQ/retry topics for both Kafka and Rabbit consumers to avoid message loss.
- Move SMTP credentials & frontend URLs to a secret manager instead of static configs.



