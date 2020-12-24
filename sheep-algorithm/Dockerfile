FROM openjdk:11-jre-slim
COPY ./target/*.jar /app/target.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/target.jar"]