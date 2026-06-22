FROM openjdk:27-ea-jdk-oracle
MAINTAINER "Ashutosh Tiwari"
COPY target/secure.jar secure.jar
CMD ["java","-jar","secure.jar"]