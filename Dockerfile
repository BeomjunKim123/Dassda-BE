FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
ENV ITEM_IMG_LOCATION /var/ssda/items/item/
ENV UPLOAD_PATH file:/var/ssda/items/