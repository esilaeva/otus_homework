pipeline {
    agent any

    stages {
        stage('Check Docker Version') {
            steps {
                script {
                    echo 'Checking Docker version...'
                    sh 'docker --version'
                }
            }
        }
    }
}