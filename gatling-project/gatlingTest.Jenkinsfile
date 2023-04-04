pipeline {
    agent any
    stages {
        stage("Build Maven") {
            steps {
                sh 'sudo mvn -f /home/vbarysiu/Repos/Performance-Testing-CI/gatling-project/pom.xml -B clean package'
            }
        }

    stage('Pull Latest Code'){
             steps {
                git branch: 'main',
                url: 'git@github.com:volha-barysiuk/Performance-Testing-CI.git'
             }
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
