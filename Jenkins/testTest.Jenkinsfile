node {

parameters {
    string(name: 'threads', defaultValue: '10', description: 'Number of threads')
    string(name: 'rampup', defaultValue: '5', description: 'Ramp up period in seconds')
    string(name: 'duration', defaultValue: '120', description: 'Test duration in seconds')
}

 stage("Configure") {
        sh "mkdir $WORKSPACE/$BUILD_NUMBER/"
        sh "sudo chown -R jenkins:jenkins /var/lib/jenkins/workspace"

}

    stage('Pull Latest Code'){
                git branch: 'main',
                credentialsId: '36eec0bf-90e8-447c-8dac-e1fbcbf14c35',
                url: 'git@github.com:volha-barysiuk/Performance-Testing-CI.git'
        }
        
 stage('Run test'){
 sh "sudo mkdir -p /tmp/reports"
 sh "sudo /home/vbarysiu/JMETER/apache-jmeter-5.5/bin/./jmeter -Jjmeter.save.saveservice.output_format=csv -Jthreads=${threads} -Jrampup=${rampup} -Jduration=${duration} -n -t $WORKSPACE/$BUILD_NUMBER/Performance-Testing-CI/JMeter/ci-shopizer-script.jmx -l /tmp/reports/JMeter.jtl -e -o /tmp/reports/HtmlReport"
 }


 stage('Publish results'){
 sh "sudo mv /tmp/reports/* $WORKSPACE/$BUILD_NUMBER/"
 archiveArtifacts artifacts: "${env.BUILD_NUMBER}/JMeter.jtl, ${env.BUILD_NUMBER}/HtmlReport/**/*.*", allowEmptyArchive: 'true', caseSensitive: 'false'
    } 
  }
