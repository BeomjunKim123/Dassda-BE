FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
RUN if [ ! -d /home/appdata/items/item/ ]; then \
    mkdir -p /home/appdata/items/item/ && \
    chmod -R 755 /home/appdata/items/item/; \
    fi
ENTRYPOINT ["java","-jar","/app.jar"]