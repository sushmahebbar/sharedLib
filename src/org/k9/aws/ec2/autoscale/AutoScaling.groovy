package org.k9.aws.ec2.autoscale
import org.k9.*

class AutoScaling implements Serializable {
    def config
    def script
    // def output

    AutoScaling(script, config) {
        this.script = script
        this.config = config
        // this.output = output
    }
 
 def createLaunchConfig() {
      // this.config.launchconfig.jsonBody['launchConfigurationName'] = "Lc"+env.BUILD_NUMBER
      // this.config.launchConfig.jsonBody['userData'] = "https://s3-us-west-2.amazonaws.com/${output.global['userDataScript']}/scriptViaGroovy.sh"
        //this.config.launchConf['httpParams'] = this.script.awsVars.lcHttpParams

        def lcOut = new http.SimpleHTTPBuilder(this.script,this.config.launchconfig)
        lcOut.sendRequest()
 }
 def createAutoscaling() {
      this.config.asg['jsonBody']['targetGroupARNs'] = $'tgARN'
        
     def asgOut = new http.SimpleHTTPBuilder(this.script,this.config.asg)
     asgOut.sendRequest()
        
 }

}