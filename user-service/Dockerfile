# Build
FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /app
COPY ./user-service ./user-service
WORKDIR /app/user-service
RUN mvn clean package -DskipTests

# Run
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/user-service/target/*.jar user-service.jar
EXPOSE 9004
ENTRYPOINT ["java", "-jar", "user-service.jar"]
