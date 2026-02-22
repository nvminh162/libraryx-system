# Build
FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /app

COPY ./common-service ./common-service
COPY ./notification-service ./notification-service

WORKDIR /app/common-service
RUN mvn clean install -DskipTests

WORKDIR /app/notification-service
RUN mvn clean package -DskipTests

# Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/notification-service/target/*.jar notification-service.jar
EXPOSE 9005
ENTRYPOINT ["java", "-jar", "notification-service.jar"]