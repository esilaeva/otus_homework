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
    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    // Создание и очистка директорий для отчетов
                    sh 'mkdir -p ${ALLURE_RESULTS} && rm -rf ${ALLURE_RESULTS}/*'
                    sh 'mkdir -p ${ALLURE_REPORT} && rm -rf ${ALLURE_REPORT}/*'
                }
            }
        }
        stage('Build and Test') {
            steps {
                script {
                    // Запуск Docker контейнера и выполнение команд внутри него
                    sh '''
                        CONTAINER_ID=$(docker run --privileged -d \
                            -v ${MAVEN_LOCAL_REPO}:${MAVEN_LOCAL_REPO} 192.168.88.193:5005/uitests:1.0 \
                            /bin/bash -c "rm -rf ${DOCKER_HOME}/allure-results/* ${DOCKER_HOME}/allure-report/* && \
                            mvn clean test -Denv=remote -Dmaven.repo.local=${MAVEN_LOCAL_REPO} && \
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
                withCredentials([string(credentialsId: 'chatID', variable: 'CHAT_ID'), string(credentialsId: 'token', variable: 'TOKEN')]) {
                    sh """
                        curl -X POST -H 'Content-Type: application/json' -d '{
                            "chat_id": "${CHAT_ID}",
                            "text": "${env.MESSAGE}\\n${env.REPORT_SUMMARY}",
                            "disable_notification": false
                        }' https://api.telegram.org/bot${TOKEN}/sendMessage
                    """
                }
            }
        }
    }
}