timeout(60) {
    node("maven-cloud") {
        def jobDescription = """
            Test job description
        """
        def testContainerName = "ui_tests_${env.BUILD_NUMBER}"
        env.MY_PARAM = env.MY_PARAM ?: ""

        prepareConfig()

        try {
            stage("Run UI tests ${jobDescription}") {
                sh "docker run --rm --network=host --name ${testContainerName} -v ${pwd()}/allure-results:/home/ubuntu/target/allure-results -t localhost:5005/ui-tests"
            }
            stage("Publish Allure report") {
                allure([
                        disabled: true,
                        results: ["${pwd()}/allure-results"]
                ])
            }
            stage("Telegram notification") {
                def allureReport = readFile(file: "${pwd()}/allure-results/export/influxDbData.txt")
                Allure.addAttachment("Allure Report", "text/plain", allureReport)
            }
        } finally {
            sh "docker stop ${testContainerName}"
        }
    }
}

def prepareConfig() {
    def yamlConfig = readYaml(text: env.YAML_CONFIG)
    yamlConfig.each { k, v -> System.setProperty(k, v) }
}

def triggerJob(def jobName, def config) {
    return build(job: jobName, parameters: [string(name: 'YAML_CONFIG', value: config)])
}