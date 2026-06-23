FROM maven:3 AS build
WORKDIR /app
COPY src ./src
COPY pom.xml .
RUN mvn clean package -DskipTests
FROM openjdk:27-ea-jdk-oracle
COPY --from=build /app/target/*.jar secure.jar
ENTRYPOINT ["java","-jar","secure.jar"]