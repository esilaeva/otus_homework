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
        stage('java') {
            steps {
                sh '''
                    env | grep -e PATH -e JAVA_HOME
                    which java
                    java -version
                '''
            }
        }
        stage('Clone Repository') {
            steps {
                git branch: "$env.BRANCH", url: 'https://github.com/esilaeva/otus_homework.git'

                    sh 'mvn clean test'
            }
            post {
                always {
                    allure includeProperties:
                            false,
                            jdk: '',
                            results: [[path: 'build/allure-results']]
                }
            }
        }
        // stage("Check Docker") {
        //     steps {
        //         script {
        //             def dockerVersion = sh(script: 'docker --version', returnStatus: true)
        //             if (dockerVersion != 0) {
        //                 error "Docker is not installed or not found in PATH"
        //             }
        //         }
        //     }
        // }
        stage('Create Allure Report') {
            // agent {
            //     docker {
            //         image 'apitests'
            //         args '-v /home/jenkins/.m2:/root/.m2'
            //     }
            // }
            steps {
                script {
                    def testContainerName = "apitests_${env.BUILD_NUMBER}"
                    //sh "docker run --network=host --name ${testContainerName} -v /home/jenkins/.m2:/root/.m2 -t localhost:5005/apitests"
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