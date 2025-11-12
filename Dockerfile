FROM eclipse-temurin:20-jdk
WORKDIR /app
COPY target/bank_rest-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]