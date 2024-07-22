pipeline {
    agent {
        label 'maven-cloud'
    }
    environment {
        MAVEN_LOCAL_REPO = "${WORKSPACE}/.m2/repository"
        ALLURE_RESULTS = "${WORKSPACE}/allure-results"
        ALLURE_REPORT = "${WORKSPACE}/allure-report"
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
    }
    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    // Создание директорий и установка прав доступа
                    sh 'mkdir -p ${ALLURE_RESULTS}'
                    sh 'mkdir -p ${ALLURE_REPORT}'
                }
            }
        }
        stage('Clone Repository') {
            steps {
                script {
                    sh """
                        echo "${MAVEN_CONFIG_HOME}"
                        git clone -b ${env.BRANCH} https://github.com/esilaeva/otus_homework.git 
                    """
                }
            }
        }
        stage('Run Tests') {
            steps {
                script {
                    sh """
                        mvn -Dmaven.repo.local=${MAVEN_CONFIG_HOME}  clean test -Denv=remote
                    """
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
                env.MESSAGE = "API tests ${buildStatus.toLowerCase()} for build #${env.BUILD_NUMBER}"
                try {
                    def summaryFile = "${ALLURE_REPORT}/widgets/summary.json"
                    if (fileExists(summaryFile)) {
                        def summary = sh(script: "cat ${summaryFile}", returnStdout: true).trim()
                        def jsonSlurper = new groovy.json.JsonSlurper()
                        def summaryJson = jsonSlurper.parseText(summary)
                        def passed = summaryJson.statistic.passed
                        def failed = summaryJson.statistic.total - summaryJson.statistic.passed - summaryJson.statistic.skipped
                        def skipped = summaryJson.statistic.skipped
                        def total = summaryJson.statistic.total
                        def broken = summaryJson.statistic.broken
                        env.REPORT_SUMMARY = "Passed: ${passed}, Failed: ${failed}, Skipped: ${skipped}, Broken: ${broken} \nTotal: ${total}"
                    } else {
                        env.REPORT_SUMMARY = "Summary report not found: ${summaryFile}"
                    }
                } catch (Exception e) {
                    env.REPORT_SUMMARY = "Failed to read Allure report: ${e.message}"
                }
                sh """
                    curl -X POST -H 'Content-Type: application/json' -d '{
                        "chat_id": "${env.CHAT_ID}",
                        "text": "${env.MESSAGE}\n${env.REPORT_SUMMARY}",
                        "disable_notification": false
                    }' https://api.telegram.org/bot${env.TOKEN}/sendMessage
                """
            }
        }
    }
}