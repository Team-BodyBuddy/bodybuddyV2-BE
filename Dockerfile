#베이스 도커 이미지
FROM openjdk:17-jdk-alpine

#작업 디렉토리
WORKDIR /app

#"gradle clean bootJar" 결과를 도커 컨테이너 내부에 복사
COPY ./build/libs/*.jar app.jar

#Build 관련 라벨 설정
ARG BUILD_BRANCH
ARG BUILD_DATE
ARG BUILD_NUMBER
LABEL build.branch ${BUILD_BRANCH}
LABEL build.date ${BUILD_DATE}
LABEL build.number ${BUILD_NUMBER}

#DB 접속을 위한 환경변수 설정
ENV MYSQL_URL "localhost"
ENV MYSQL_PORT "3306"
ENV MYSQL_USER "test"
ENV MYSQL_PW "3"

#JWT 토큰을 위한 환경변수 설정
ENV auth.secret-key "secret"

#스프링 프로파일 설정
ENV SPRING_PROFILES_ACTIVE "local"

#시간대 설정
ENV TZ "Asia/Seoul"

#서비스 포트 실행
EXPOSE 8080

#spring boot 실행
CMD java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar app.jar
