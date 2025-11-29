FROM gradle:8-jdk21 AS builder
WORKDIR /app

COPY --chown=gradle:gradle . /app
RUN gradle clean backend:order-service:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

COPY --from=builder /app/backend/order-service/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]