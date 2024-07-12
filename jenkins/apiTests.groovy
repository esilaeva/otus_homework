timeout(60) {
    node("maven-cloud") {
        def testContainerName = "apitests_$BUILD_NUMBER"

//        try {
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
//                docker run --network=host -v ./.m2:/root/.m2 localhost:5005/apitests:1.0

            }
//            stage("Publish allure report") {
//                allure([
//                        disabled: true,
//                        results: ["${pwd}/allure-results"]
//                ])
//            }
//            stage("Telegram notification") {
//                def allureReport = readFile(file: "${pwd}/allure-results/export/influxDbData.txt")
//                Allure.addAttachment("Allure Report", "text/plain", allureReport)
//            }
//        } finally {
//            sh "docker stop ${testContainerName}"
//        }
    }
}
