package org.k9.aws.ec2.loadbalancing
import org.k9.*

class LoadBalancer implements Serializable {
    def config
    def script

    LoadBalancer(script, config) {
        this.script = script
        this.config = config
    }
 def createLoadbalancer() {

      // this.config.elb['jsonBody']['name'] = "${this.config.deploymentName}-${this.config.subEnv}-elb-${output.dbData.version}-${output.dbData.buildNo}"
        //this.config.elb['httpParams'] = this.script.awsVars.elbHttpParams

        def elbOut = new http.SimpleHTTPBuilder(this.script,this.config.elb)
        def value=elbOut.sendRequest()
        return value['elbARN']
        
}
def createTargetGroup() {
        
        def tgOut = new http.SimpleHTTPBuilder(this.script,this.config.targetgroup)
        def out=tgOut.sendRequest()
        this.script.echo " targrt group:${out}"
        
        return out['tgARN']
       
}
def createELBListener(def output,def arg) {
     this.config.elbListener['jsonBody']['defaultActions'] = [['targetGroupArn': output]]
     this.config.elbListener['jsonBody']['loadBalancerArn']=arg
       
    def listenerOut = new http.SimpleHTTPBuilder(this.script,this.config.elbListener)
    listenerOut.sendRequest()  
}
 
}