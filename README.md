# Автоматизация тестирования для OTUS

Репозиторий содержит автоматизированные тесты, написаны в рамках курса ["Java QA Engineer. Professional"](https://otus.ru/lessons/java-qa-pro/).

## Технологии и иструменты
Автотесты написаны на языке `Java` с использованием `JUnit 5`, `Selenium WebDriver`. Сборщик проекта - `Maven`. 
<p align="center">
    <a href="https://www.java.com/">
      <img width="10%" title="Java" src="src/main/resources/media/java-original.svg" alt="java">
    </a>
    <a href="https://www.jetbrains.com/">
      <img width="10%" title="IntelliJ IDEA" src="src/main/resources/media/Idea.svg" alt="IntelliJ IDEA">
    </a>
    <a href="https://maven.apache.org/">
      <img width="10%" title="Maven" src="src/main/resources/media/ApacheMaven.svg" alt="Maven">
    </a>
    <a href="https://junit.org/junit5/">
      <img width="10%" title="JUnit5" src="src/main/resources/media/Junit5.svg" alt="JUnit5">
    </a>
    <a href="https://github.com/">
      <img width="10%" title="GitHub" src="src/main/resources/media/github-mark-white.svg" alt="GitHub">
    </a>
    <a href="https://www.selenium.dev">
      <img width="10%" title="Selenium WebDriver" src="src/main/resources/media/Selenium.svg" alt="Selenium WebDriver">
    </a>
    <a href="https://www.google.com/intl/en/chrome/">
      <img width="10%" title="Chrome" src="src/main/resources/media/Chrome.svg" alt="Chrome">
    </a>
    <a href="https://www.mozilla.org/en-US/firefox/new/">
      <img width="10%" title="Firefox" src="src/main/resources/media/Firefox.svg" alt="Firefox">
    </a>
</p>

### Команда для запуска автотестов из терминала
Для запуска тестов в браузере `Chrome` необходимо в терминале вызвать команду:  
`mvn clean test -Dbrowser=chrome`  

Для запуска тестов в браузере `Firefox` необходимо в терминале вызвать команду:  
`mvn clean test -Dbrowser=firefox`