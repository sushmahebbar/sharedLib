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
            //this.script.echo "====== ${lcOut} "
            if (lcOut['response'] == "success"){
                this.script.echo "LC created :: ${lcOut}"
            }else{
                this.script.echo "LC error!!"
                status = [response: "error", msg: "LC error!!"]
                return
            }

            def tgresponse = elb.createTargetGroup()
            //this.script.echo "====== ${tgARN} "
            
            def elbOut = elb.createLoadbalancer()
            this.script.echo "====== ${elbOut} "
            if (elbOut['response'] == "success"){
                //copyMapData(elbOut)
                this.script.echo "ELB created"
            }else{
                this.script.echo "ELB error!!"
                status = [response: "error", msg: "ELB error!!"]
                return
            }
             def elbListenerOut = elb.createELBListener(tgresponse,elbOut)
            if (elbListenerOut['response']=="success"){
                this.script.echo "elb listener created"
            }else {
                this.script.echo "elb listener error"
            }
           
            def asgOut = asg.createAutoscaling(tgresponse)
            this.script.echo "====== ${asgOut} "
           
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