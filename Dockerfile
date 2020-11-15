FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} PGR301-DevOps-Exam-H2020-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","PGR301-DevOps-Exam-H2020-1.0-SNAPSHOT.jar"]