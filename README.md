# Автоматизация тестирования для OTUS

Репозиторий содержит автоматизированные тесты, написаны в рамках курса ["Java QA Engineer. Professional"](https://otus.ru/lessons/java-qa-pro/).

## Ветвление в репозитории

- `main`: основная ветка. Содержит тесты, разработанные с применением двухуровневого подхода к дизайну тестов и с
  использованием паттернов проектирования.
- `homework_2`: ветка, содержащая тесты, написанные с использованием **BDD** подхода через **Cucumber**.

## Технологии и иструменты

Автотесты написаны на языке `Java` с использованием `JUnit 5`, `Selenium WebDriver`, `Cucumber`. Сборщик
проекта - `Maven`.
<p align="center">
    <a href="https://www.java.com/">
      <img width="8%" title="Java" src="src/main/resources/media/java-original.svg" alt="java">
    </a>
    <a href="https://www.jetbrains.com/">
      <img width="8%" title="IntelliJ IDEA" src="src/main/resources/media/Idea.svg" alt="IntelliJ IDEA">
    </a>
    <a href="https://maven.apache.org/">
      <img width="8%" title="Maven" src="src/main/resources/media/ApacheMaven.svg" alt="Maven">
    </a>
    <a href="https://junit.org/junit5/">
      <img width="8%" title="JUnit5" src="src/main/resources/media/Junit5.svg" alt="JUnit5">
    </a>
    <a href="https://github.com/">
      <img width="8%" title="GitHub" src="src/main/resources/media/github-mark-white.svg" alt="GitHub">
    </a>
    <a href="https://www.selenium.dev">
      <img width="8%" title="Selenium WebDriver" src="src/main/resources/media/Selenium.svg" alt="Selenium WebDriver">
    </a>
    <a href="https://www.google.com/intl/en/chrome/">
      <img width="8%" title="Chrome" src="src/main/resources/media/Chrome.svg" alt="Chrome">
    </a>
    <a href="https://www.mozilla.org/en-US/firefox/new/">
      <img width="8%" title="Firefox" src="src/main/resources/media/Firefox.svg" alt="Firefox">
    </a>
    <a href="https://cucumber.io/">
      <img width="8%" title="Cucumber" src="src/main/resources/media/Cucumber.svg" alt="Cucumber">
    </a>
</p>

## Команда для запуска автотестов из терминала
Чтобы запустить тесты, реализованные по методологии **BDD (Cucumber)**, необходимо переключиться на ветку `homework_2`
командой:
```bash
git checkout homework_2
```

Для запуска всех тестов команда (по умолчанию запуститься браузер `Chrome`):  
`mvn clean test`

Для запуска всех тестов в **выбранном браузере** команда:
#### Chrome
`mvn clean test -Dbrowser=chrome`

#### Firefox
`mvn clean test -Dbrowser=firefox`



