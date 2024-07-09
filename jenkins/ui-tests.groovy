timeout(60) {
    node("maven") {
        def testContainerName = "ui_tests_$BUILD_NUMBER"

        env.MY_PARAM = env.MYPARAM ?: ""

        prepareConfig()
        try {
            wrap([$class: 'BuildUser']) {
                currentBuild.description = "User: $BUILD_USER"
            }
            stage("Run UI tests ${jobDescription}") {
                sh "docker run --rm --network=host --name ${testContainerName} -v $pwd/allure-results:/home/ubuntu/target/allure-results -t localhost:5005/ui-tests"
            }
            stage("Publish allure report") {
                allure {[
                        disabled: true,
                        results: ["$pwd/allure-results"]
                ]
                }
            }
            stage("Telegram notification") {
                def allureReport = readFile(text: "$pwd/allure-result/export/influxDbData.txt")
                Allure.addAttachment("Allure Report", "text/plain", allureReport)

            }
        } finally {
            sh "docker stop $testContainerName"
        }
    }
}

def prepareConfig() {
    def yamlConfig = readYaml(text: "$YAML_CONFIG")
    yamlConfig.each{k, v -> System.setProperty(v)}
}

def triggerJob(def jobName, def config) {
    return build(job: "$jobName", parameters: ["YAML_CONFIG": config])
}