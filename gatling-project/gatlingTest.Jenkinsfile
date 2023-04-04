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
                credentialsId: '36eec0bf-90e8-447c-8dac-e1fbcbf14c35',
                url: 'git@github.com:volha-barysiuk/Performance-Testing-CI.git'
             }
        }

     stage("Run Gatling") {
            steps {
                sh 'sudo mvn -f /home/vbarysiu/Repos/Performance-Testing-CI/gatling-project/pom.xml gatling:test'
            }

            post {
                always {
                    gatlingArchive()
                }
            }
        }
    }
}
