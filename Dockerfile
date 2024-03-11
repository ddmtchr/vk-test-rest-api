FROM maven:3.9.5-eclipse-temurin-17 as build

WORKDIR /usr/src/app
COPY pom.xml .
COPY src ./src
RUN mvn clean package
ENTRYPOINT ["java","-jar","target/vk-test-rest-api-0.0.1-SNAPSHOT.jar"]
