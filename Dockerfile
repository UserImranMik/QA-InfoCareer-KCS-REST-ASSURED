FROM openjdk:17

WORKDIR /app

COPY target/RestAssured-Framework.jar RestAssured-Framework.jar

ENTRYPOINT ["java", "-jar", "/app/RestAssured-Framework.jar"]