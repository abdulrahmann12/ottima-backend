# ==========================================
# Stage 1: Build the Spring Boot Application
# ==========================================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copy Maven POM first to leverage Docker layer caching
COPY pom.xml .

# Pre-fetch project dependencies
RUN mvn dependency:go-offline -B || true

# Copy application source code
COPY src ./src

# Build production jar without running tests
RUN mvn clean package -DskipTests -B

# ==========================================
# Stage 2: Lightweight Production Runtime
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Hugging Face Spaces requires running as user with UID 1000
RUN addgroup -g 1000 appgroup && \
    adduser -u 1000 -G appgroup -s /bin/sh -D appuser && \
    chown -R appuser:appgroup /app

# Copy compiled jar from builder stage
COPY --from=builder --chown=appuser:appgroup /build/target/*.jar app.jar

# Switch to non-root user
USER appuser

# Configure container environment variables & default port (HF Spaces defaults to 7860)
ENV PORT=7860 \
    SERVER_PORT=7860 \
    JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 7860

# Launch application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
