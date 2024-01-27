FROM openjdk:17-alpine
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
# /app/items 디렉토리 생성 및 권한 설정
RUN mkdir -p /app/items && \
    chmod 755 /app/items

# 작업 디렉토리 설정
WORKDIR /app
ENTRYPOINT ["java","-jar","/app.jar"]