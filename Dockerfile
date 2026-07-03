FROM eclipse-temurin:21-jdk
COPY target/FoodMaster-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]