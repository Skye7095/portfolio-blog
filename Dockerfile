FROM openjdk:17.0.2-jdk-slim-buster AS builder

WORKDIR /app

COPY build/libs/blog.jar /app/

CMD ["java", "-jar", "blog.jar"]