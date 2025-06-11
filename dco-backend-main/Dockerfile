FROM eclipse-temurin:17-jdk-alpine

# 필요한 패키지 설치 및 Python 설정
RUN apk add --no-cache python3 py3-pip

WORKDIR /app
COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
