FROM openjdk:11
ADD target/AdminAPI-0.0.1-SNAPSHOT.jar admin-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "admin-api.jar"]
