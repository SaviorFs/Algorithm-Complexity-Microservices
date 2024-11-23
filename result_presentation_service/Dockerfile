# Build stage
FROM gradle:7.6.1-jdk11 AS build
WORKDIR /app
COPY . /app
RUN gradle clean build --no-daemon

# Run stage
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/build/libs/result_presentation_service-1.0-SNAPSHOT.jar app.jar
EXPOSE 5002
ENTRYPOINT ["java", "-jar", "app.jar"]
