FROM azul/zulu-openjdk:21

LABEL authors="krzys"

WORKDIR /app

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]