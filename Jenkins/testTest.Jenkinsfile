pipeline {
    agent any

parameters {
    string(name: 'minUsers', defaultValue: '1', description: 'Users to ramp from')
    string(name: 'maxUsers', defaultValue: '20', description: 'Users to ramp to')
    string(name: 'rampTime', defaultValue: '1', description: 'Ramp duration (minutes)')
    string(name: 'constTime', defaultValue: '1', description: 'Constant duration (minutes)')
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
                credentialsId: 'f2874c35-b597-428b-9c7f-d4bb9f0f15fa',
                url: 'git@github.com:volha-barysiuk/Performance-Testing-CI.git -DminUsers=${minUsers} -DmaxUsers=${maxUsers} -DrampTime=${rampTime} -DconstTime=${constTime}'
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

