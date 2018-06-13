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
 
 def createLaunchConfig(def output) {
        def funcOutput = [:]
        this.config.launchConf['jsonBody']['launchConfigurationName'] = "${this.config.deploymentName}-${this.config.subEnv}-lc-${output.dbData.version}-${output.dbData.buildNo}"
        this.config.launchConf['jsonBody']['userData'] = "https://s3-us-west-2.amazonaws.com/${output.global['userDataScript']}/scriptViaGroovy.sh"
        //this.config.launchConf['httpParams'] = this.script.awsVars.lcHttpParams

        def lcOut = new http.SimpleHTTPBuilder(this.script,this.config.launchConf).sendRequest()
        if (lcOut['response'] == "error") {
                funcOutput['response'] = "error"
                return funcOutput
        }

        funcOutput['response'] = "success"
        funcOutput['global'] = ['launchConfName': this.config.launchConf['jsonBody']['launchConfigurationName'] ]
 }