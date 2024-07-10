FROM maven:3.9.8

USER root

RUN mkdir -p /home/tests/ui-test

WORKDIR /home/tests/ui-test

COPY . .

ENTRYPOINT ["./entrypoint.sh"]