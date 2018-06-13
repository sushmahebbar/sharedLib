package org.k9.aws.ami

import groovy.time.*
import org.k9.*

class Utils implements Serializable {
    def config
    def script
    def output

    Utils(script, config) {
        this.script = script
        this.config = config
    }
def createAWSResources(def asg, def elb){
       
            def lcOut = asg.createLaunchConfig()
            this.script.echo "====== ${lcOut} "
            if (lcOut['response'] == "success"){
                this.script.echo "LC :: ${this.output}"
            }else{
                this.script.echo "LC error!!"
                status = [response: "error", msg: "LC error!!"]
                return
            }

            def tgOut = elb.createTargetGroup()
            this.script.echo "====== ${tgOut} "
             if (tgOut['response'] == "success"{
            this.script.echo "TG :: ${this.output}" 
            }else{
                this.script.echo "TG error!!"
                status = [response: "error", msg: "TG error!!"]
                return
            }
            def elbOut = elb.createLoadbalancer()
            this.script.echo "====== ${elbOut} "
            if (elbOut['response'] == "success"){
                //copyMapData(elbOut)
                this.script.echo "ELB :: ${this.output}"
            }else{
                this.script.echo "ELB error!!"
                status = [response: "error", msg: "ELB error!!"]
                return
            }
            // def elbListenerOut = elb.createELBListener(this.output)
            // this.script.echo "====== ${elbListenerOut} "
            // if (elbListenerOut['response'] == "success"){
            //     copyMapData(elbListenerOut)
            //     this.script.echo "ELBLISTENER :: ${this.output}"
            // }else{
            //     this.script.echo "ELBLISTENER error!!"
            //     status = [response: "error", msg: "ELBLISTENER error!!"]
            //     return
            // }
            // def asgOut = asg.createAutoscaling(this.output)
            // this.script.echo "====== ${asgOut} "
            // if (asgOut['response'] == "success"){
            //     copyMapData(asgOut)
            //     this.script.echo "ASG :: ${this.output}"
            // }else{
    //             this.script.echo "ASG error!!"
    //             status = [response: "error", msg: "ASG error!!"]
    //             return
    //         }
    //         // if (depStrategy == bg) {
    //         //     ///////////
    //         // }
    //     }
    //     if(status['response'] == "error"){
    //             return status
    //     }
    //     return status
    // }
}
}