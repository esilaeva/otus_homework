FROM maven:3.9.8

USER root

DOCKER_OPTS="-H tcp://0.0.0.0:2375"

RUN mkdir -p /home/tests/api-test

WORKDIR /home/tests/api-test

COPY . .

ENTRYPOINT ["./entrypoint.sh"]