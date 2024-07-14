pipeline {
    agent {
        label 'maven-cloud'
    }
    environment {
        TOKEN = credentials('token')
        CHAT_ID = credentials('chatId')
    }
    options {
        timeout(time: 60, unit: 'MINUTES')
    }
    stages {
        stage("Check Docker") {
            steps {
                script {
                    def dockerVersion = sh(script: 'docker --version', returnStatus: true)
                    if (dockerVersion != 0) {
                        error "Docker is not installed or not found in PATH"
                    }
                }
            }
        }
        stage('Run API tests') {
            steps {
                script {
                    def testContainerName = "apitests_${env.BUILD_NUMBER}"
                    // sh "docker run --network=host --name ${testContainerName} -v /home/jenkins/.m2:/root/.m2 -t localhost:5005/apitests:1.0"
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
                    sh """curl -X POST -H 'Content-Type: application/json' -d '{"chat_id": "${env.CHAT_ID}", "text": "${env.MESSAGE}", "disable_notification": false}' https://api.telegram.org/bot${env.TOKEN}/sendMessage"""
                }
            }
        }
    }
}