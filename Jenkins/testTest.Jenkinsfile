pipeline {
    agent any

parameters {
    string(name: 'threads', defaultValue: '10', description: 'Number of threads')
    string(name: 'rampup', defaultValue: '5', description: 'Ramp up period in seconds')
    string(name: 'duration', defaultValue: '120', description: 'Test duration in seconds')
}
 
    stages {
    
     stage("Build Maven") {
            steps {
                sh 'sudo mvn -f $WORKSPACE/Gatling/pom.xml -B clean package'
            }
        }
    
    
    stage('Pull Latest Code'){
             steps {
                sh "sudo chown -R jenkins:jenkins /var/lib/jenkins/workspace"
                git branch: 'main',
                credentialsId: '36eec0bf-90e8-447c-8dac-e1fbcbf14c35',
                url: 'git@github.com:volha-barysiuk/Performance-Testing-CI.git'
             }
        }


        stage("Configure Workspace") {
            steps {
                sh "mkdir $WORKSPACE/$BUILD_NUMBER"
            }
        }
        


     stage("Run Gatling") {
            steps {
                sh 'sudo mvn -f $WORKSPACE/Gatling/pom.xml gatling:test'
            }
            
                        post {
                always {
                    gatlingArchive()
                }
            }
        }


    }
}

