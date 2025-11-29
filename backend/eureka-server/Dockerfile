FROM gradle:8-jdk21 AS builder
WORKDIR /app

COPY --chown=gradle:gradle . /app
RUN gradle clean backend:eureka-server:bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

COPY --from=builder /app/backend/eureka-server/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]