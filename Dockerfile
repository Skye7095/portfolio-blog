FROM openjdk:17.0.2-jdk-slim-buster AS builder

WORKDIR /app
COPY gradle ./gradle
COPY src/main ./src/main

RUN ./gradlew --8.3
RUN ./gradlew bootJar

FROM openjdk:17.0.2-slim-buster

WORKDIR /app
COPY --from=builder /app/build/libs/blog-*.jar app.jar

ENV PROFILE="dev"

ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE