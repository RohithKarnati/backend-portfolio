# --- Build stage ---
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN ./gradlew --no-daemon dependencies || true

COPY src ./src
RUN ./gradlew --no-daemon clean bootJar

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 9000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
