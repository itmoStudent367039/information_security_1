pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        sh './gradlew build -x test'
      }
    }

    stage('Snyk Security Scan') {
      environment {
        SNYK_TOKEN = credentials('snyk-api-token')  // тип: Secret text
      }
      steps {
        nodejs(nodeJSInstallationName: 'node18') {
          sh 'snyk test --json > snyk-results.json || true'
          sh 'snyk-to-html -i snyk-results.json -o snyk-report.html'
        }

        archiveArtifacts artifacts: 'snyk-report.html,snyk-results.json', allowEmptyArchive: true
      }
    }
  }
}