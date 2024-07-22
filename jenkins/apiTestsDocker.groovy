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
    }
    stages {
        stage('Build and Test') {
            steps {
                script {
                    // Запуск Docker контейнера и выполнение команд внутри него
                    sh '''
                        CONTAINER_ID=$(docker run --privileged -d \
                            -v ${MAVEN_LOCAL_REPO}:/home/jenkins/workspace/test/.m2/repository \
                            192.168.88.193:5005/apitests-success:1.1 \
                            /bin/bash -c "rm -rf /home/tests/api-test/allure-results/* /home/tests/api-test/allure-report/* && \
                            mvn clean test -Dmaven.repo.local=/home/jenkins/workspace/test/.m2/repository && \
                            allure generate /home/tests/api-test/allure-results --clean -o /home/tests/api-test/allure-report")
                        
                        # Просмотр логов выполнения тестов
                        docker logs -f $CONTAINER_ID
                        
                        # Копирование содержимого результатов и отчетов из контейнера
                        docker cp $CONTAINER_ID:/home/tests/api-test/allure-results/. ${ALLURE_RESULTS}/
                        docker cp $CONTAINER_ID:/home/tests/api-test/allure-report/. ${ALLURE_REPORT}/
                    '''
                }
            }
        }
    }
    post {
        always {
            script {
                sh '''
                    docker stop $CONTAINER_ID || true
                    docker rm $CONTAINER_ID || true
                '''

                // Генерация отчета Allure с помощью плагина Jenkins
                allure includeProperties: false, jdk: '', reportBuildPolicy: 'ALWAYS', results: [[path: "${ALLURE_RESULTS}"]]

                // Подготовка и отправка сообщения в Telegram
                def buildStatus = currentBuild.currentResult
                env.MESSAGE = "${env.JOB_NAME} ${buildStatus.toLowerCase()} for build #${env.BUILD_NUMBER}\n****************************************"
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
                        env.REPORT_SUMMARY = "Passed: ${passed}, Failed: ${failed}, Error: ${error} , Skipped: ${skipped}\nTotal: ${total}"
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
