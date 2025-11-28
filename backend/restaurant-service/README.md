# 🍽️ Restaurant Service

> Powers restaurant discovery, menu management, and media assets for the platform—covering both public APIs and internal data for other services.

## 🗂️ Responsibilities

| Domain | Capabilities |
| --- | --- |
| Restaurants | CRUD, status transitions, search/filter endpoints. |
| Dishes | CRUD, status validation, association with restaurants. |
| Images | Upload and manage restaurant/dish photos via Imgbb. |
| Internal APIs | `/internal/restaurants/**` feed canonical data to order-service. |

## 🧱 Architecture

- **Controllers:** `RestaurantController` (public) + `InternalRestaurantController` (service-to-service).
- **Services:** `RestaurantService`, `DishService`, `ImageFacadeService` encapsulate transactional logic.
- **Image module:** Facade → handler → uploader pattern; handlers enforce per-entity limits, uploader calls Imgbb through `WebClient`.
- **Persistence:** Spring Data JPA repositories (`RestaurantRepository`, `DishRepository`, `ImageRepository`) + MapStruct mappers in `mapper/`.
- **Validation:** Custom annotations (`@AllowedRestaurantStatus`, `@AllowedDishStatus`) guard request DTOs.

## 🗃️ Data & Infra

| Component | Role |
| --- | --- |
| 🐘 PostgreSQL (`restaurantdb`) | Restaurants, dishes, images (Liquibase migrations). |
| 🖼️ Imgbb API | Stores binary content; service keeps URLs + metadata only. |
| ⚙️ Config/Eureka | Supplies DB creds, image limits, Imgbb API key, discovery data. |

## 🔐 Security Model

- `HeaderAuthenticationFilter` authenticates end-users via gateway headers.
- `InternalHeaderAuthenticationFilter` protects `/internal/**` with service tokens.
- `SecurityConfig` profiles:  
  - **dev:** GET endpoints open to everyone, mutations require `ROLE_ADMIN`, Swagger enabled.  
  - **prod:** Swagger disabled, same RBAC, `/internal/**` limited to `ROLE_INTERNAL`.

## 🖼️ Image Flow

1. Client posts multipart image.
2. `ImageFacadeService` selects handler (restaurant vs dish).
3. Handler validates ownership + enforces limits from `ImageLimitProperties`.
4. `ImageUploaderImpl` streams data to Imgbb (`/1/upload?key=<API_KEY>`) and parses response URL.
5. URL + metadata persisted in Postgres and tied to target entity.

## 🏃 Run It Locally

1. `docker compose up postgres-restaurant`
2. Start Config Server (with `config-repo/restaurant-service-dev.yaml`) + Eureka.
3. Provide `IMGBB_API_KEY` env var or override in config repo.
4. Launch:  
   `./gradlew :backend:restaurant-service:bootRun --args='--spring.profiles.active=dev'`
5. Port: `8082`
6. Key routes:  
   - Public: `/api/v1/restaurants`, `/api/v1/restaurants/{id}`, `/api/v1/restaurants/{id}/dishes`  
   - Internal: `/internal/restaurants/{id}`, `/internal/restaurants/{id}/dishes`

## 🧪 Testing & Recommendations

- `./gradlew :backend:restaurant-service:test`
- Add integration tests mocking Imgbb, verifying dish ownership, and status transitions.
- Introduce retry/fallback for Imgbb outages + quota monitoring.

## 📈 Observability & TODOs

- Actuator health/info exposed—hook into tracing/metrics pipeline.
- Move Imgbb secrets & limits to a secret manager.
- Add domain-level authorization checks (ownership) to complement HTTP RBAC.



