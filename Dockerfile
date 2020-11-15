FROM maven:3.6-jdk-11 as builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn package -DskipTests

COPY target/PGR301-DevOps-Exam-H2020-1.0-SNAPSHOT.jar .
CMD java -jar ./PGR301-DevOps-Exam-H2020-1.0-SNAPSHOT.jar