node {

parameters {
    string(name: 'threads', defaultValue: '10', description: 'Number of threads')
    string(name: 'rampup', defaultValue: '5', description: 'Ramp up period in seconds')
    string(name: 'duration', defaultValue: '120', description: 'Test duration in seconds')
}


stage('Pull Latest Code'){
                sh "sudo chown -R jenkins:jenkins /var/lib/jenkins/workspace"
                git branch: 'main',
                credentialsId: 'f2874c35-b597-428b-9c7f-d4bb9f0f15fa',
                url: 'git@github.com:volha-barysiuk/Performance-Testing-CI.git'
        }
        
 stage("Configure") {
        sh "mkdir $WORKSPACE/$BUILD_NUMBER/"
}

        
 stage('Run test'){
 sh "sudo mkdir -p /tmp/reports"
 sh "sudo /home/vbarysiu/JMETER/apache-jmeter-5.5/bin/./jmeter -Jjmeter.save.saveservice.output_format=csv -Jthreads=${threads} -Jrampup=${rampup} -Jduration=${duration} -n -t $WORKSPACE/JMeter/ci-shopizer-script.jmx -l /tmp/reports/JMeter.jtl -e -o /tmp/reports/HtmlReport"
 }


 stage('Publish results'){
 sh "sudo mv /tmp/reports/* $WORKSPACE/$BUILD_NUMBER/"
 archiveArtifacts artifacts: "${env.BUILD_NUMBER}/JMeter.jtl, ${env.BUILD_NUMBER}/HtmlReport/**/*.*", allowEmptyArchive: 'true', caseSensitive: 'false'
    } 
  }
