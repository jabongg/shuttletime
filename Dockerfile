# ==============================
# Stage 1: Build with Maven
# ==============================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml and download dependencies first (caching)
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# Copy source code and build
COPY src src
RUN ./mvnw clean package -DskipTests

# ==============================
# Stage 2: Run the app
# ==============================
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the jar file from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the Spring Boot app
CMD ["java", "-jar", "app.jar"]
