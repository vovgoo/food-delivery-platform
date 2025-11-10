FROM gradle:8-jdk21 AS builder
WORKDIR /app

COPY --chown=gradle:gradle . /app
RUN gradle clean bootJar --no-daemon

FROM eclipse-temurin:21-jdk-slim
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
