pipeline {
    agent {
        label 'maven-cloud'
    }
    environment {
        MAVEN_LOCAL_REPO = "${WORKSPACE}/.m2/repository"
        ALLURE_RESULTS = "${WORKSPACE}/allure-results"
        ALLURE_REPORT = "${WORKSPACE}/allure-report"
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
        TOKEN = credentials('token')
        CHAT_ID = credentials('chatID')
        JOB_NAME = "${env.JOB_NAME}"
        DOCKER_HOME = "/home/ubuntu/ui-test"
    }
    parameters {
        text(name: 'YAML_CONFIG', defaultValue: '', description: 'YAML Configuration')
    }
    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    sh 'mkdir -p ${ALLURE_RESULTS} && rm -rf ${ALLURE_RESULTS}/*'
                    sh 'mkdir -p ${ALLURE_REPORT} && rm -rf ${ALLURE_REPORT}/*'

                    def configText = params.YAML_CONFIG
                    def configMap = [:]
                    configText.split('\n').each { line ->
                        def parts = line.split(':', 2)
                        if (parts.length == 2) {
                            configMap[parts[0].trim()] = parts[1].trim()
                        }
                    }

                    env.BASE_URL = configMap['BASE_URL'] ?: ''
                    env.BROWSER_NAME = configMap['BROWSER_NAME'] ?: ''
                    env.BROWSER_VERSION = configMap['BROWSER_VERSION'] ?: ''
                    env.IS_REMOTE = configMap['IS_REMOTE'] ?: ''
                    env.REMOTE_URL = configMap['REMOTE_URL'] ?: ''

                    echo "Configuration parsed successfully: BASE_URL=${env.BASE_URL}, BROWSER_NAME=${env.BROWSER_NAME}, BROWSER_VERSION=${env.BROWSER_VERSION}, REMOTE_URL=${env.REMOTE_URL}"
                }
            }
        }
        stage('Build and Test') {
            steps {
                script {
                    sh '''
                        # Запуск Docker контейнера и выполнение команд внутри него
                        CONTAINER_ID=$(docker run --privileged -d \
                            -v ${MAVEN_LOCAL_REPO}:${MAVEN_LOCAL_REPO} \
                            192.168.88.193:5005/uitests:1.0 \
                            /bin/bash -c "rm -rf ${DOCKER_HOME}/allure-results/* ${DOCKER_HOME}/allure-report/* && \
                            mvn clean test -DbaseUrl=${BASE_URL} -DbrowserName=${BROWSER_NAME} -DbrowserVersion=${BROWSER_VERSION} -DremoteUrl=${REMOTE_URL} -DisRemote=${IS_REMOTE} -Dmaven.repo.local=${MAVEN_LOCAL_REPO} && \
                            allure generate ${DOCKER_HOME}/allure-results --clean -o ${DOCKER_HOME}/allure-report")
                        
                        # Просмотр логов выполнения тестов
                        docker logs -f $CONTAINER_ID

                        # Копирование содержимого результатов и отчетов из контейнера
                        docker cp $CONTAINER_ID:/home/ubuntu/ui-test/allure-results/. ${ALLURE_RESULTS}/
                        docker cp $CONTAINER_ID:/home/ubuntu/ui-test/allure-report/. ${ALLURE_REPORT}/

                        docker stop $CONTAINER_ID || true
                        docker rm $CONTAINER_ID || true
                    '''
                }
            }
        }
    }
    post {
        always {
            script {
                // Генерация отчета Allure с помощью плагина Jenkins
                allure includeProperties: false, jdk: '', reportBuildPolicy: 'ALWAYS', results: [[path: "${ALLURE_RESULTS}"]]

                // Подготовка и отправка сообщения в Telegram
                def buildStatus = currentBuild.currentResult
                env.MESSAGE = "${env.JOB_NAME} ${buildStatus.toLowerCase()} for build #${env.BUILD_NUMBER}"
                def allureReportUrl = "${env.BUILD_URL}allure"
                try {
                    def summaryFile = "${ALLURE_REPORT}/widgets/summary.json"
                    if (fileExists(summaryFile)) {
                        def summary = sh(script: "cat ${summaryFile}", returnStdout: true).trim()
                        def jsonSlurper = new groovy.json.JsonSlurper()
                        def summaryJson = jsonSlurper.parseText(summary)
                        def passed = summaryJson.statistic.passed
                        def failed = summaryJson.statistic.failed
                        def skipped = summaryJson.statistic.skipped
                        def total = summaryJson.statistic.total
                        def error = total - passed - failed - skipped
                        env.REPORT_SUMMARY = "Passed: ${passed}, Failed: ${failed}, Skipped: ${skipped}, Error: ${error} \nTotal: ${total}"
                    } else {
                        env.REPORT_SUMMARY = "Summary report not found: ${summaryFile}"
                    }
                } catch (Exception e) {
                    env.REPORT_SUMMARY = "Failed to read Allure report: ${e.message}"
                }
                sh """
                    curl -X POST -H 'Content-Type: application/json' -d '{
                        "chat_id": "${env.CHAT_ID}",
                        "text": "${env.MESSAGE}\\n${env.REPORT_SUMMARY}\\nAllure Report: ${allureReportUrl}",
                        "disable_notification": false
                    }' https://api.telegram.org/bot${env.TOKEN}/sendMessage
                """
            }
        }
    }
}
