# Étape 1 : builder
FROM gradle:8-jdk17 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build -x test

# Étape 2 : runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
