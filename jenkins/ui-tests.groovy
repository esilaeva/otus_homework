pipeline {
    agent {
        label 'maven-cloud'
    }
    environment {
        TOKEN = credentials('token')
        CHAT_ID = credentials('chatID')
        //MAVEN_LOCAL_REPO = "/var/jenkins_home/.m2/repository"
        BRANCH = 'main'
    }
    options {
        timeout(time: 60, unit: 'MINUTES')
    }
    stages {
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
                        mkdir -p ${MAVEN_CONFIG_HOME}
                        chown -R jenkins:jenkins ${MAVEN_CONFIG_HOME}
                        chmod -R 775 ${MAVEN_CONFIG_HOME}
                        mvn -Dmaven.repo.local=${MAVEN_CONFIG_HOME} -f otus_homework/pom.xml clean test
                    """
                }
            }
            post {
                always {
                    allure includeProperties: false, jdk: '', results: [[path: 'otus_homework/allure-results']]
                }
            }
        }
        stage('Create Allure Report') {
            agent none
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