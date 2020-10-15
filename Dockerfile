FROM openjdk:11
ADD target/TheaterAPI-0.0.1-SNAPSHOT.jar theater-api.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "theater-api.jar"]
