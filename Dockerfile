FROM maven:3.9.8

USER root

RUN mkdir -p /home/tests/api-test

RUN apt-get update && apt-get install -y docker.io

WORKDIR /home/tests/api-test

COPY . .

ENTRYPOINT ["./entrypoint.sh"]