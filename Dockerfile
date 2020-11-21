FROM maven:3.6-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package

FROM openjdk:8-jdk-alpine
COPY --from=builder /app/target/*.jar /app/PGR301-DevOps-Exam-H2020-1.0-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/app/PGR301-DevOps-Exam-H2020-1.0-SNAPSHOT.jar"]