FROM maven:3.9.8

USER root

RUN mkdir -p /home/ubuntu/ui-test

RUN apt-get update && apt-get install -y docker.io

WORKDIR /home/ubuntu/ui-test

COPY . .

RUN apt install -y wget sudo
RUN apt-get update && \
    apt-get install -y wget gnupg2 unzip curl && \
    wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list' && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    CHROMEDRIVER_VERSION=$(curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE) && \
    wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/$CHROMEDRIVER_VERSION/chromedriver_linux64.zip && \
    unzip /tmp/chromedriver.zip chromedriver -d /usr/local/bin/ && \
    rm /tmp/chromedriver.zip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

RUN wget -qO- --show-progress https://github.com/allure-framework/allure2/releases/download/2.27.0/allure-2.27.0.tgz | tar -zx -C /opt/
ENV PATH=$PATH:/opt/allure-2.27.0/bin

CMD ["mvn", "clean", "test", "-Denv=remote"]

#ENTRYPOINT ["./entrypoint.sh"]
