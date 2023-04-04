pipeline {
    agent any
    stages {
        stage("Build Maven") {
            steps {
                sh 'mvn -B clean package'
            }
        }

    stage('Pull Latest Code'){
                git branch: 'main',
                url: 'git@github.com:volha-barysiuk/Performance-Testing-CI.git'
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
