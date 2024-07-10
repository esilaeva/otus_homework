FROM maven:3.9.8

USER root

RUN mkdir -p /home/tests/api-test

WORKDIR /home/tests/api-test

COPY . .

ENTRYPOINT ["./entrypoint.sh"]