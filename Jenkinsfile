pipeline {
  agent any
  tools{
    maven 'Maven 3.9.11'
    jdk 'temurin-24'
  }
  options {
    timestamps()
    ansiColor('xterm')
  }

  parameters {
    // Which branch to checkout
    string(name: 'GIT_BRANCH', defaultValue: 'main', description: 'Git branch to build')

    string(name: 'FILES', defaultValue: 'testng.xml', description: 'TestNG XML file(s) in src/test/resources (comma-separated)')

    // Choose tests by **groups** (TestNG)
    // e.g. smoke,regression
    string(name: 'GROUPS', defaultValue: '', description: 'Optional TestNG groups (comma-separated)')

    // Choose tests by **packages** (TestNG suite <packages>)
    // e.g. tests.*  OR  tests.t1.*   (used only when FILES is empty)
    string(name: 'PACKAGES', defaultValue: 'tests.*', description: 'Optional TestNG package selector')

    // Who to notify
    string(name: 'EMAIL_TO', defaultValue: 'wagdy.abdelrahman@gmail.com', description: 'Email recipients (comma-separated)')
  }

  environment {
    MVN = 'mvn -B -U'
    // filtered testng.xml ends up here after testResources phase
    SUITE_XML = 'target/test-classes/testng.xml'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout([$class: 'GitSCM',
          branches: [[name: "*/${params.GIT_BRANCH}"]],
          userRemoteConfigs: [[
            url: 'https://github.com/Abdelrahman-Wagdy/Rest-APIs.git',   // <-- change me
//             credentialsId: 'github-creds'                 // <-- change/remove if public
          ]]
        ])
      }
    }

    stage('Build') {
      steps {
        sh "${env.MVN} -q -DskipTests clean package"
      }
    }

    stage('Test') {
          steps {
            script {
              def args = []
              if (params.GROUPS?.trim())   args << "-Dgroups=${params.GROUPS.trim()}"
              if (params.PACKAGES?.trim()) args << "-DtestPackage=${params.PACKAGES.trim()}"

              def suiteFiles = params.FILES.split(',').collect { it.trim() }.findAll { it }

              for (f in suiteFiles) {
                echo ">>> Running suite: ${f}"
                sh """
                  set -e
                  ${env.MVN} clean test -DsuiteXmlFile=${env.TEST_RESOURCES}/${f} ${args.join(' ')}
                """
              }
            }
          }
        }
      }

  post {
    always {
      // Publish Allure (requires Allure Jenkins plugin)
      always {
              allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
            }

      // Archive useful artifacts
      sh 'zip -qr allure-results.zip target/allure-results || true'
      archiveArtifacts artifacts: 'allure-results.zip, target/surefire-reports/**/*', fingerprint: true, allowEmptyArchive: true
    }
    success {
      emailext(
        to: params.EMAIL_TO,
        subject: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
        mimeType: 'text/html',
        body: """
          <p>✅ <b>${env.JOB_NAME} #${env.BUILD_NUMBER}</b> on branch <code>${params.GIT_BRANCH}</code> succeeded.</p>
          <ul>
            <li><a href="${env.BUILD_URL}">Build page</a></li>
            <li><a href="${env.BUILD_URL}allure/">Allure report</a></li>
            <li><a href="${env.BUILD_URL}artifact/allure-results.zip">Allure results (zip)</a></li>
          </ul>
          <p>Params:<br/>
            FILES=<code>${params.FILES}</code><br/>
            GROUPS=<code>${params.GROUPS}</code><br/>
            PACKAGES=<code>${params.PACKAGES}</code>
          </p>
        """,
        attachmentsPattern: 'allure-results.zip'
      )
    }
    failure {
      emailext(
        to: params.EMAIL_TO,
        subject: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
        mimeType: 'text/html',
        body: """
          <p>❌ <b>${env.JOB_NAME} #${env.BUILD_NUMBER}</b> on branch <code>${params.GIT_BRANCH}</code> failed.</p>
          <ul>
            <li><a href="${env.BUILD_URL}console">Console log</a></li>
            <li><a href="${env.BUILD_URL}allure/">Allure (may be partial)</a></li>
            <li><a href="${env.BUILD_URL}artifact/allure-results.zip">Allure results (zip)</a></li>
          </ul>
          <p>Params:<br/>
            FILES=<code>${params.FILES}</code><br/>
            GROUPS=<code>${params.GROUPS}</code><br/>
            PACKAGES=<code>${params.PACKAGES}</code>
          </p>
        """,
        attachmentsPattern: 'allure-results.zip'
      )
    }
  }
}
