pipeline {
  agent any

  tools {
    jdk 'jdk17' // Убедитесь, что в Jenkins настроена JDK с этим именем
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
      steps {
        snykSecurity(
          snykInstallation: 'snyk@latest', // Имя инсталляции Snyk CLI в Jenkins Global Tools
          snykTokenId: 'snyk-api-token', // ID credentials типа "Secret text" в Jenkins Credentials
          additionalArguments: '--sarif-file-output=snyk-results.sarif --all-projects'
        )
        // Загрузка SARIF-результата как артефакта
        archiveArtifacts artifacts: 'snyk-results.sarif', allowEmptyArchive: true
      }
    }
  }
}
