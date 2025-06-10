FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/gf-dashboard-service-0.0.1-SNAPSHOT.jar gf-dashboard-service-0.0.1-SNAPSHOT.jar

ARG SPRING_PROFILE
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILE}

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "gf-dashboard-service-0.0.1-SNAPSHOT.jar"]
