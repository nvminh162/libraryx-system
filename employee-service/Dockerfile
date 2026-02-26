# Build
FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /app

COPY ./common-service ./common-service
COPY ./employee-service ./employee-service

WORKDIR /app/common-service
# Has JAR
RUN mvn clean install package -DskipTests
WORKDIR /app/employee-service
# No JAR
RUN mvn clean package -DskipTests

# Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/employee-service/target/*.jar employee-service.jar
EXPOSE 9002
ENTRYPOINT ["java", "-jar", "employee-service.jar"]
