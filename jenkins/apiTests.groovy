pipeline {
    agent {
        label 'maven-cloud'
    }
    environment {
        TOKEN = credentials('token')
        CHAT_ID = credentials('chatId')
        MAVEN_LOCAL_REPO = "${WORKSPACE}/.m2/repository"
    }
    options {
        timeout(time: 60, unit: 'MINUTES')
    }
    stages {
        stage('Pull Docker Image and Run Tests') {
            steps {
                script {
                    sh 'docker pull 192.168.88.207:5005/apitests:1.0'
                    sh """
                        docker run --rm -v ${MAVEN_LOCAL_REPO}:/root/.m2/repository 192.168.88.207:5005/apitests:1.0 mvn clean test
                    """
                }
            }
            post {
                always {
                    allure includeProperties: false, jdk: '', results: [[path: 'build/allure-results']]
                }
            }
        }
        stage('Create Allure Report') {
            steps {
                script {
                    env.MESSAGE = "API tests completed successfully for build #${env.BUILD_NUMBER}"
                }
            }
            post {
                failure {
                    script {
                        env.MESSAGE = "API tests failed for build #${env.BUILD_NUMBER}"
                    }
                }
            }
        }
        stage('Telegram Notification') {
            steps {
                script {
                    sh """
                        curl -X POST -H 'Content-Type: application/json' -d '{
                            "chat_id": "${env.CHAT_ID}",
                            "text": "${env.MESSAGE}",
                            "disable_notification": false
                        }' https://api.telegram.org/bot${env.TOKEN}/sendMessage
                    """
                }
            }
        }
    }
}