FROM maven:3.8.8-eclipse-temurin-17 as builder

WORKDIR /app

COPY pom.xml .
COPY src ./src


RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-slim

WORKDIR /app

RUN mkdir -p /app/summary-logs && chmod -R 777 /app/summary-logs


COPY --from=builder /app/target/*.jar app.jar
COPY Wallet_LLWLBZ7KFC99X64K ./Wallet_LLWLBZ7KFC99X64K


EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
