FROM openjdk:17-slim AS build
WORKDIR /workspace/app

# Copy the environment file
COPY .env .env

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-slim
WORKDIR /app
COPY --from=build /workspace/app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"] 