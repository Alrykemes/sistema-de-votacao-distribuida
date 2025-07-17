FROM eclipse-temurin:23-jdk AS builder

WORKDIR /app

COPY mvnw ./
COPY .mvn ./.mvn
COPY pom.xml ./
COPY src ./src

RUN apt-get update && apt-get install -y dos2unix && dos2unix mvnw
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Final image
FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY --from=builder /app/target/votacao-distribuida-*.jar app.jar

ARG MAIN_CLASS
ENV MAIN_CLASS=${MAIN_CLASS}

ENTRYPOINT ["sh", "-c", "java -cp app.jar ${MAIN_CLASS}"]
