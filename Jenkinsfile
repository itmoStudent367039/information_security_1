pipeline {
  agent any

  tools {
    jdk 'jdk17' // Убедитесь, что в Jenkins настроена JDK с этим именем
    snyk 'snyk-tool'
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
            SNYK_TOKEN = credentials('snyk-api-token')  // тип: Secret text
          }
          steps {
            sh 'snyk test --sarif-file-output=snyk-results.sarif --all-projects'
            archiveArtifacts artifacts: 'snyk-results.sarif', allowEmptyArchive: true
          }
        }
  }
}
