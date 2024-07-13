timeout(60) {
    node("maven-cloud") {
        def testContainerName = "apitests_$BUILD_NUMBER"
        def telegramToken = credentials('token')
        def chatId = credentials('chatId')
        def message = ""

        try {
            stage("Check Docker") {
                script {
                    def dockerVersion = sh(script: 'docker --version', returnStatus: true)
                    if (dockerVersion != 0) {
                        error "Docker is not installed or not found in PATH"
                    }
                }
            }
            stage("Run API tests") {
                sh "docker run --network=host --name ${testContainerName} -v /home/jenkins/.m2:/root/.m2 -t localhost:5005/apitests:1.0"
                message = "API tests completed successfully for build ${BUILD_NUMBER}"
            }
        } catch (Exception e) {
            message = "API tests failed for build ${BUILD_NUMBER}: ${e.message}"
            throw e
        } finally {
            // Отправляем уведомление в Telegram
            sh """
        curl -s -X POST https://api.telegram.org/bot${telegramToken}/sendMessage \\
            -d chat_id=${chatId} \\
            -d text="${message}"
    """
        }
    }
}