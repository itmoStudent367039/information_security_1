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
                checkout scm // scm автоматически содержит правильные credentials из конфигурации джобы
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

Подробный отчёт: [Build #${BUILD_NUMBER}](${BUILD_URL})
                    """.stripIndent()
                }
            }
        }

        stage('Comment PR') {
            when {
                expression { env.CHANGE_ID != null } // только для PR
            }
            steps {
                script {
                    // GitHub Branch Source автоматически устанавливает статус коммита
                    // Для комментария используем curl (githubComment требует отдельного плагина)
                    sh """
                        curl -s -X POST \
                          -H "Authorization: token ${env.GITHUB_TOKEN}" \
                          -H "Accept: application/vnd.github.v3+json" \
                          https://api.github.com/repos/${env.CHANGE_AUTHOR}/${env.CHANGE_TARGET}/issues/${env.CHANGE_ID}/comments \
                          -d '{"body": "${env.VULN_REPORT}\\n\\nПодробный отчёт: [Jenkins Build #${BUILD_NUMBER}](${BUILD_URL})"}'
                    """
                }
            }
        }

        stage('Publish Report') {
            steps {
                // Показываем "плашку" прямо в логах сборки
                echo "\n${env.VULN_REPORT}\n"

                // Сохраняем как артефакт
                archiveArtifacts artifacts: 'vuln-report.txt'
            }
        }
    }
}
