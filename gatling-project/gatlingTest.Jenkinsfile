pipeline {
    agent any
    stages {
        stage("Build Maven") {
            steps {
                sh 'mvn -B clean package'
            }
        }

 stage('pullLatestCode'){
                git branch: 'main',
                 credentialsId: '86***e40-8583-4850-bcf5-24***b2e6b57',
                 url: 'git@ssh.dev.azure.com:v3/AzureDevOpsOrg/Project-AzureDevOps/perf-testing'
        }

        stage("Run Gatling") {
            steps {
                sh 'mvn gatling:test'
            }

            post {
                always {
                    gatlingArchive()
                }
            }
        }
    }
}
