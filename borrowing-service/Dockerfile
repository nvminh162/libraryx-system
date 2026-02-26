# Build
FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /app

COPY ./common-service ./common-service
COPY ./borrowing-service ./borrowing-service

WORKDIR /app/common-service
# Has JAR
RUN mvn clean install package -DskipTests
WORKDIR /app/borrowing-service
# No JAR
RUN mvn clean package -DskipTests

# Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/borrowing-service/target/*.jar borrowing-service.jar
EXPOSE 9003
ENTRYPOINT ["java", "-jar", "borrowing-service.jar"]
