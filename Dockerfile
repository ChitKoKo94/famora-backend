FROM eclipse-temurin:21.0.7_6-jdk-alpine-3.21

WORKDIR /app

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
