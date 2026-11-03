pipeline {
    agent any
    tools {
        maven 'Maven 3.9.11'
        jdk 'temurin-24'
    }
    options {
        timestamps()
        disableConcurrentBuilds()
    }
    parameters {
        string(name: 'FILES', defaultValue: 'testng.xml', description: 'Suite filenames in src/test/resources, comma-separated; blank uses testng.xml')
        string(name: 'GROUPS', defaultValue: '', description: 'Optional TestNG groups, comma-separated')
        string(name: 'PACKAGES', defaultValue: 'tests.*', description: 'TestNG package selector')
        string(name: 'EMAIL_TO', defaultValue: '', description: 'Optional report recipients; blank disables email')
    }
    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Test') {
            steps {
                script {
                    def files = params.FILES.split(',').collect { it.trim() }.findAll { it }
                    if (!files) { files = ['testng.xml'] }
                    for (file in files) {
                        if (!(file ==~ /[A-Za-z0-9_-]+\.xml/)) {
                            error("Invalid suite filename: ${file}")
                        }
                        if (!fileExists("src/test/resources/${file}")) {
                            error("Suite not found: ${file}")
                        }
                    }
                    // Maven filters resources before Surefire reads these suites.
                    def suites = files.collect { "target/test-classes/${it}" }.join(',')
                    withEnv([
                        "SELECTED_SUITES=${suites}",
                        "SELECTED_PACKAGE=${params.PACKAGES.trim() ?: 'tests.*'}",
                        "SELECTED_GROUPS=${params.GROUPS.trim()}"
                    ]) {
                        sh '''
                            if [ -n "$SELECTED_GROUPS" ]; then
                                mvn -B clean test "-Dsurefire.suiteXmlFiles=$SELECTED_SUITES" "-DtestPackage=$SELECTED_PACKAGE" "-Dgroups=$SELECTED_GROUPS"
                            else
                                mvn -B clean test "-Dsurefire.suiteXmlFiles=$SELECTED_SUITES" "-DtestPackage=$SELECTED_PACKAGE"
                            fi
                        '''
                    }
                }
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/surefire-reports/**/*, target/allure-results/**/*', allowEmptyArchive: true
            script {
                if (fileExists('target/allure-results')) {
                    allure includeProperties: false, results: [[path: 'target/allure-results']]
                }
                if (params.EMAIL_TO?.trim()) {
                    emailext(
                        to: params.EMAIL_TO.trim(),
                        subject: "${currentBuild.currentResult}: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                        body: "Build: ${env.BUILD_URL}\nAllure: ${env.BUILD_URL}allure/",
                        mimeType: 'text/plain'
                    )
                }
            }
        }
    }
}
