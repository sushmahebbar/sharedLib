package org.k9.aws.ami

import groovy.time.*
import java.text.*
import org.k9.*
import org.k9.aws.ec2.autoscale.AutoScaling
import org.k9.aws.ec2.loadbalancing.LoadBalancer

class AMIDeployment implements Serializable {
    def config
    def script
   // def output

    AMIDeployment(script, config) {
        this.script = script
        this.config = config
       // this.output = [:]
    }

    // def CreateLaunchconfig(){
    // def httpObj = new http.SimpleHTTPBuilder(this.script,this.params.launchconfig)
    // httpObj.sendRequest()
    // }
def deploy() {
     def utilobj = new Utils(this.script,this.config, this.output)
     def asg = new AutoScaling(this.script, this.config)
     def elb = new LoadBalancer(this.script, this.config)
     def createStatus = utilobj.createAWSResources(asg, elb)
}
}