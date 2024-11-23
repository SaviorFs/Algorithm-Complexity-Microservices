# Build stage
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app

# Copy the Maven POM file
COPY pom.xml .

# Download dependencies to cache them
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy the packaged JAR from the build stage
COPY --from=build /app/target/recurrence_relation_service-1.0-SNAPSHOT.jar app.jar

# Expose the port the service runs on
EXPOSE 5001

# Healthcheck for service availability
HEALTHCHECK --interval=30s --timeout=10s CMD curl --fail http://localhost:5001/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
