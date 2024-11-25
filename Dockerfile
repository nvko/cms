FROM openjdk:17-jdk-slim
COPY target/cms.jar cms.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]