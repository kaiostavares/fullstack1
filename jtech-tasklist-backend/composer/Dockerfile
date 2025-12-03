# Build stage
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# Copy gradle files
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

# Download dependencies
RUN gradle dependencies --no-daemon || true

# Copy source code
COPY src ./src

# Build application
RUN gradle bootJar --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Create non-root user
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

# Copy jar from build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8081

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
