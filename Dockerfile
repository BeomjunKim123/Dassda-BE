FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
WORKDIR /app/items
ENTRYPOINT ["java","-jar","/app.jar"]