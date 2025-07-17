FROM openjdk:17-slim

WORKDIR /app

COPY target/centralserver.jar centralserver.jar

EXPOSE 1099

CMD ["java", "-jar", "centralserver.jar"]
