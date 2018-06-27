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
 def fetchCode() {
        try {
            def builds = [:]
            this.script.echo "BEFORE LOOP: ${this.output['global']['workSpace']}"

            def codeDir, duration

            this.config.scm.each {
                builds["fetch ${it.key}"] = {
                    this.script.node {
                            codeDir=""
                            if ( ("${it.key}" == "code" && "${this.config.language}" == "go")|| ("${it.key}" == "bddTest") ) {
                                codeDir = "src/github.com/${it.key}"
                            }
                            this.script.dir ("${this.output['global']['workSpace']}/${it.key}/"+codeDir) {
                                new scm.Git(this.script,this.config.scm."${it.key}".git).checkout(stage: "fetch-code")
                            }
                        
                            // duration = this.script.utils.elapsed(timeStart)   // this gives Non Serializable exception
                           // this.script.echo "fetch ${it.key} ${duration}"
                    }
                }
            }
            this.script.echo "AFTER LOOP : ${this.output['global']['workSpace']}"
            this.script.echo "builds: ${builds}"
            this.script.stage('Checkout'){
                this.script.parallel builds
            }
            // duration = elapsed(fetchCodetimeStart)
            // this.script.echo "fetch Func Duration :::: ${duration}"
            return [response: "success"]
        }catch(Exception fetchFuncEx){
            this.script.echo "(In fetch code) :: error !${fetchFuncEx}"
            return [response: "error"]
        }
    }

    // def deployProperties() {
    //     try {
    //         def deploytimeStart = new Date()
    //         def builds = [:]
    //         def out
    //         this.config.scm.each {
    //             if ("${it.key}" == "code"{
    //                 return
    //             }
    //             builds["deploy ${it.key}"] = {
    //                 this.script.node {
    //                     def timeStart = new Date()
    //                     def propertyFile

    //                     def s3Bucket = this.config.s3['binaryBucket']
                    
    //                     this.script.dir ("${this.output.global['workSpace']}/${it.key}") {
    //                         switch ("${it.key}") {
    //                             case "appProp":
    //                             case "envProp":
    //                             case "secrets":
    //                                 if ("${it.key}" == "appProp") {
    //                                     propertyFile = "properties.json"
    //                                 }else if ("${it.key}" == "envProp") {
    //                                     propertyFile = "environment.json"
   
    //                                 }else if ("${it.key}" == "secrets") {
    //                                     propertyFile = "secret.json"
    //                                 }
    //                                 out = this.script.sh(returnStatus: true, 
                                                            //  script: """
            //                                                  echo `pwd`
            //                                                  aws s3 cp ${propertyFile} s3://${s3Bucket}/live/
            //                                                  """)
            //                                 // script: "aws s3 cp * s3://${s3Bucket}/live/${propertyFile}")
            //                         break
            //                 }
            //                 if ( out != 0 ) {
            //                     this.script.error "deploy ${it.key} :: error"
            //                 }
            //             }  
            //         }
            //     }
            // }
            // this.script.echo "(In deploy properties) :: builds: ${builds}"

    //         this.script.stage('deploy properties') {
    //             this.script.parallel builds
    //         }
    //         def duration = elapsed(deploytimeStart)
    //         this.script.echo "(In deploy properties) :: deploy prop Func Duration :::: ${duration}"
    //         return [response: "success"]
    //     }catch (Exception fetchFuncEx){
    //         this.script.echo "(In deploy properties) :: error !${fetchFuncEx}"
    //         return [response: "error"]
    //     }
    // }
    @NonCPS
    def writeToFile(def fileName, def binaryBucket, def binaryFile){
        try{
            new File(fileName).withWriter('utf-8') { writer -> 
                writer.writeLine """
#!/bin/bash
mkdir -p ~/app
aws s3 cp s3://${binaryBucket} ~/app --recursive
cd ~/app
chmod u+x ${binaryFile}
nohup ./${binaryFile} & > nohup.out
                """
            }
        }catch (Exception writeToFileEx){
            this.script.echo "Write to file error ${writeToFileEx}"
            return "error"
        }
    }

    def copyBinaryFileToS3() {
        this.script.echo "In copyBinaryFileToS3"
    
        def binaryFile, codeDir, out
        if ( this.config.language == "go"){
            binaryFile = "code"
            codeDir = "code/src/github.com/code"
        }

        //def binaryBucketSuffix = "/${this.config.deploymentName}/${this.config.subEnv}/binary/${this.config.deploymentStrategy}/${this.config.version}"
        def binaryBucket = this.config.s3['binaryBucket']

        //def scriptBucketSuffix = "/${this.config.deploymentName}/${this.config.subEnv}/script/${this.config.deploymentStrategy}/${this.config.version}"
        def scriptBucket = this.config.s3['scriptBucket'] 

        this.output.global += ['userDataScript': scriptBucket]
        def tmpFile = 'scriptViaGroovy.sh'
        writeToFile(tmpFile,binaryBucket,binaryFile)

        out = this.script.sh(returnStatus: true, 
                            script: """
                                aws s3 cp ${System.getProperty("user.dir")}/${tmpFile} s3://${scriptBucket}/
                                aws s3 cp ${codeDir}/${binaryFile} s3://${binaryBucket}/
                            """)
        if (out == 0){
            return [response: "success"]
        }
        return [response: "error"]
    }

def createAWSResources(def asg, def elb){
       
            def lcOut = asg.createLaunchConfig()
            //this.script.echo "====== ${lcOut} "
            if (lcOut['response'] == "success"){
                this.script.echo "LC created "
            }else{
                this.script.echo "LC error!!"
                status = [response: "error", msg: "LC error!!"]
                return
            }

            def tgresponse = elb.createTargetGroup()
            //this.script.echo "====== ${tgARN} "
            
            def elbOut = elb.createLoadbalancer()
            this.script.echo "====== ${elbOut} "
            // if (elbOut['response'] == "success"){
            //     //copyMapData(elbOut)
            //     this.script.echo "ELB created"
            // }else{
            //     this.script.echo "ELB error!!"
            //     status = [response: "error", msg: "ELB error!!"]
            //     return
            // }
            def asgOut = asg.createAutoscaling(tgresponse)
            this.script.echo "====== ${asgOut} "
           
         def elbListenerOut = elb.createELBListener(tgresponse,elbOut)
         this.script.echo " elb listener created:${elbListenerOut}"
            //  if (elbListenerOut['response']=="success"){
            //      this.script.echo "elb listener created"
            // }else {
            //      this.script.echo "elb listener error"
            // }
           
           
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