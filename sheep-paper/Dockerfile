FROM maven:3.6.3-jdk-11 AS build
COPY src /code/app/src
COPY pom.xml /code/app
RUN mvn -f /code/app/pom.xml clean package

FROM openjdk:11-jre-slim
COPY --from=build /code/app/target/*.jar /app/target.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/target.jar"]