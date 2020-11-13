#!/bin/bash
    echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
    docker build . --tag pgr301-exam-monsters --build-arg JAR_FILE=./target/pgr301-exam-monsters
    docker tag  pgr301-exam-monsters  jonpus/pgr301-exam-monsters
    docker push jonpus/pgr301-exam-monsters