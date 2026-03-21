
# stage 1
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

# Build jar

RUN mvn clean package -DskipTests

# Stage 2
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/rev_pay_p2-0.0.1-SNAPSHOT.jar app.jar
ENV SERVER_PORT=8081
EXPOSE 8081

CMD ["java", "-jar", "app.jar"]
