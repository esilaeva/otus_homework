import jenkins/utils.groovy

timeout(60) {
    node("maven") {
        def testContainerName = "api_tests_$BUILD_NUMBER"

        env.MY_PARAM = env.MYPARAM ?: ""

        prepareConfig()
        try {
            wrap([$class: 'BuildUser']) {
                currentBuild.description = "User: $BUILD_USER"
            }
            stage("Run API tests ${jobDescription}") {
                sh "docker run --rm --network=host --name ${testContainerName} -v $pwd/allure-results:/home/ubuntu/target/allure-results -t api-tests"
            }
            stage("Publish allure report") {
                allure {[
                        disabled: true,
                        results: ["$pwd/allure-results"]
                ]
                }
            }
            stage("Telegram notification") {
                def allureReport = readFile test: $pwd/allure-result/export/influxDbData.txt
                def connection = new Connection()
            }
        } finally {
                sh "docker stop $testContainerName"
        }
    }
}
