# Étape 1 : build
FROM gradle:8.3-jdk17 AS builder
WORKDIR /app
COPY . .

# Donner les permissions d'exécution au wrapper Gradle
RUN chmod +x gradlew

# Build le projet en ignorant les tests
RUN ./gradlew clean build -x test

# Étape 2 : runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
