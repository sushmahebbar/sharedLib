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

       this.config.elb['jsonBody']['name'] = "${this.config.deploymentName}-${this.config.subEnv}-elb-${output.dbData.version}-${output.dbData.buildNo}"
        //this.config.elb['httpParams'] = this.script.awsVars.elbHttpParams

        def elbOut = new http.SimpleHTTPBuilder(this.script,this.config.elb)
        elbout.sendRequest()
        
}
}