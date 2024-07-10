FROM maven:3.9.8

USER root

RUN mkdir -p /home/ubuntu/ui-test

WORKDIR /home/ubuntu/ui-test

COPY . .

RUN chmod +x

ENTRYPOINT ["./entrypoint.sh"]