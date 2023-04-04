node {

parameters {
    string(name: 'threads', defaultValue: '10', description: 'Number of threads')
    string(name: 'rampup', defaultValue: '5', description: 'Ramp up period in seconds')
    string(name: 'duration', defaultValue: '120', description: 'Test duration in seconds')
}

 stage("configure") {
        sh "mkdir $WORKSPACE/$BUILD_NUMBER/"
}

 stage('run test'){
 sh "sudo mkdir -p /tmp/reports"
 sh "sudo /home/vbarysiu/JMETER/apache-jmeter-5.5/bin/./jmeter -Jjmeter.save.saveservice.output_format=csv -Jthreads=${threads} -Jrampup=${rampup} -Jduration=${duration} -n -t /home/vbarysiu/JMETER/apache-jmeter-5.5/bin/ci-shopizer-script.jmx -l /tmp/reports/JMeter.jtl -e -o /tmp/reports/HtmlReport"
 }


 stage('publish results'){
 sh "sudo mv /tmp/reports/* $WORKSPACE/$BUILD_NUMBER/"
 archiveArtifacts artifacts: "${env.WORKSPACE}/${env.BUILD_NUMBER}/*.jtl", allowEmptyArchive: 'true', caseSensitive: 'false'
    } 
  }
