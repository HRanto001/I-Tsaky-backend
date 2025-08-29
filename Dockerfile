# Étape 1 : build
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app
COPY . .

# Assure que le wrapper gradle est exécutable
RUN chmod +x gradlew

# Build le projet en ignorant les tests
RUN ./gradlew clean build -x test

# Étape 2 : runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copie le jar généré
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
