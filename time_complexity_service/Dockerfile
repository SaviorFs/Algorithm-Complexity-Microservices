# Build stage
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:11-jre-slim
WORKDIR /app

# Copy the packaged JAR from the build stage
COPY --from=build /app/target/time_complexity_service-1.0-SNAPSHOT.jar app.jar

# Expose the port your application runs on
EXPOSE 5000

# Healthcheck for the service
HEALTHCHECK --interval=30s --timeout=10s CMD curl --fail http://localhost:5000/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
