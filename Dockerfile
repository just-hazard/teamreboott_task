FROM openjdk:17-jdk-slim

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build

ARG JAR_FILE=build/libs/*SNAPSHOT.jar
RUN mv ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
