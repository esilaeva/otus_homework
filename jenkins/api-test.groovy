pipeline {
    agent {
        docker {
            image 'maven:3.8.1-jdk-11' // Укажите правильный образ Docker с Maven и JDK
            args '-v /root/.m2:/root/.m2' // Сохранение кэша Maven
        }
    }
    parameters {
        text(name: 'YAML_CONFIG', defaultValue: 'BROWSER_NAME: chrome', description: 'YAML configuration') // Добавление параметра YAML_CONFIG
        string(name: 'BRANCH', defaultValue: 'master', description: 'Branch for tempest nova API tests') // Добавление параметра BRANCH
    }
    stages {
        stage('Build') {
            steps {
                script {
                    // Убедитесь, что зависимости загружены
                    sh 'mvn clean install -DskipTests'
                }
            }
        }
        stage('Run API tests') {
            steps {
                script {
                    def testContainerName = "api_tests_${env.BUILD_NUMBER}"
                    sh "docker run --rm --network=host --name ${testContainerName} -v ${pwd()}/allure-results:/home/ubuntu/target/allure-results -t localhost:5005/api-tests"
                }
            }
        }
        stage('Publish allure report') {
            steps {
                allure([
                        disabled: true,
                        results: ["${pwd()}/allure-results"]
                ])
            }
        }
        stage('Telegram notification') {
            steps {
                script {
                    //@Grab(group='io.qameta.allure', module='allure-java-commons', version='2.13.9') // Добавлено использование @Grab для загрузки зависимости Allure
                    //import io.qameta.allure.Allure // Импорт класса Allure

                    def allureReport = readFile(file: "${pwd()}/allure-result/export/influxDbData.txt") // Исправлено использование метода readFile
                    Allure.addAttachment("Allure Report", "text/plain", allureReport)
                }
            }
        }
    }
    post {
        always {
            script {
                def testContainerName = "api_tests_${env.BUILD_NUMBER}"
                sh "docker stop ${testContainerName}"
            }
        }
    }
}

def prepareConfig() {
    def yamlConfig = readYaml(text: params.YAML_CONFIG) // Исправлено использование params.YAML_CONFIG
    yamlConfig.each { k, v -> System.setProperty(k, v) } // Исправлено использование замыкания для каждого элемента конфигурации
}

def triggerJob(def jobName, def config) {
    return build(job: "$jobName", parameters: [string(name: 'YAML_CONFIG', value: config)]) // Исправлено использование параметров для build
}