pipeline {
  agent any

  tools {
      jdk 'jdk17'
    }

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
        SNYK_TOKEN = credentials('snyk-api-token')
      }
      steps {
        nodejs(nodeJSInstallationName: 'node18') {
          sh 'snyk test --json > snyk-results.json'
          sh 'snyk-to-html -i snyk-results.json -o snyk-report.html'
        }

        archiveArtifacts artifacts: 'snyk-report.html,snyk-results.json', allowEmptyArchive: true
      }
    }
  }
}