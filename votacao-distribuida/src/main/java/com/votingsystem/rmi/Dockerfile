FROM openjdk:21-slim

WORKDIR /app

COPY . /app

RUN apt-get update && apt-get install -y findutils

# Compila corretamente com pacotes
RUN find . -name "*.java" > sources.txt && javac -d . @sources.txt

CMD ["java", "-cp", ".", "com.votingsystem.rmi.server.VotingServerMain"]
