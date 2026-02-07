pipeline {
    agent any

    environment {
        // Mock-данные для MVP
        CRITICAL = '6'
        HIGH = '12'
        LOW = '1'
        UNASSIGNED = '1'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Mock Scan') {
            steps {
                script {
                    echo "🔍 Имитация сканирования уязвимостей..."
                    sleep 2

                    // Формируем "плашку"
                    env.VULN_REPORT = "🔴 Critical: ${CRITICAL} | 🟠 High: ${HIGH} | 🟢 Low: ${LOW} | ⚪ Unassigned: ${UNASSIGNED}"

                    writeFile file: 'vuln-report.txt', text: """
${env.VULN_REPORT}

Подробный отчёт: [Jenkins Build #${BUILD_NUMBER}](${BUILD_URL})
                    """.stripIndent()

                    echo "\n${env.VULN_REPORT}\n"
                }
            }
        }

        stage('Comment PR') {
            when {
                expression { env.CHANGE_ID != null } // Только для PR
            }
            steps {
                script {
                    // Ключевой момент: используем pullRequest.comment()
                    pullRequest.comment("""
🔴 Critical: ${CRITICAL} | 🟠 High: ${HIGH} | 🟢 Low: ${LOW} | ⚪ Unassigned: ${UNASSIGNED}

Подробный отчёт: [Jenkins Build #${BUILD_NUMBER}](${BUILD_URL})
                    """.stripIndent())
                }
            }
        }
    }

    post {
        always {
            // Публикуем отчёт как артефакт
            archiveArtifacts artifacts: 'vuln-report.txt', allowEmptyArchive: true
            cleanWs()
        }
    }
}
