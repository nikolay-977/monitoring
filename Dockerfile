FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
COPY src ./src
COPY pom.xml .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
COPY --from=build target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]