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
        BRANCH = "main"
    }
    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    // Создание/очистка директорий для отчетов
                    sh 'mkdir -p ${ALLURE_RESULTS} && rm -rf ${ALLURE_RESULTS}/*'
                    sh 'mkdir -p ${ALLURE_REPORT} && rm -rf ${ALLURE_REPORT}/*'

                    // Получение параметров из YAML_CONFIG
                    def configText = params.YAML_CONFIG
                    def configMap = [:]
                    configText.split('\n').each { line ->
                        def parts = line.split(':', 2) // Split only at the first ':'
                        if (parts.length == 2) {
                            configMap[parts[0].trim()] = parts[1].trim()
                        }
                    }

                    BASE_URL = configMap['BASE_URL']
                    BROWSER_NAME = configMap['BROWSER_NAME']
                    BROWSER_VERSION = configMap['BROWSER_VERSION']
                    REMOTE_URL = configMap['REMOTE_URL']

                    echo "Configuration parsed successfully: BASE_URL=${BASE_URL}, BROWSER_NAME=${BROWSER_NAME}, BROWSER_VERSION=${BROWSER_VERSION}, REMOTE_URL=${REMOTE_URL}"
                }
            }
        }
        stage('Clone Repository') {
            steps {
                script {
                    sh """
                        echo "${MAVEN_CONFIG_HOME}"
                        git clone -b ${env.BRANCH} https://github.com/esilaeva/otus_homework.git ${WORKSPACE}/otus_homework
                    """
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    sh """
                        cd ${WORKSPACE}/otus_homework
                        mvn clean test -Dtest=RunnerTest -DbaseUrl=${BASE_URL} -DbrowserName=${BROWSER_NAME} -DbrowserVersion=${BROWSER_VERSION} -DremoteUrl=${REMOTE_URL} -Dmaven.repo.local=${MAVEN_LOCAL_REPO}
                    """
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