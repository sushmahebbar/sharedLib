@Library('test@master') _
        
import org.k9.*

pipeline {
    agent any
    stages {
        stage('create resource') {
            steps {
                script {
                    def params = [
                            launchconfig: [
                                    "jsonBody": [
                                        "imageId": "ami-f2d3638a",
                                        "instanceType": "t2.micro",
                                        "launchConfigurationName": "http.buildNo"
                                        "securityGroups": ["sg-879965f8"],
                                        "userData": "https://s3-us-west-2.amazonaws.com/hudsonbay-test/aws/dev/script/blue-green/R1/scriptViaGroovy.sh"
                                        "keyName": "HudsonBay-V",
                                        "iamInstanceProfile": "ec2-s3-RO-role"
                                    ],
                                    httpParams: [
                                        url: "http://localhost:4321",
                                        path: "/v1/launchconfig",
                                        method: "POST"
                                    ]
                                ]
                                targetgroup: [
                                    jsonBody: [
                                         "name": "testTG",
                                          "port": "8080",
                                          "protocol": "HTTP" ,
                                          "vpcId": "vpc-0f2c8a76"
                                    ],
                                    httpParams:[
                                         method: "POST",
                                         url: "http://localhost:4321",
                                         path: "/v1/targetgroup"
                                    ]
                                ]
                            ]
                      
                    def httpObj = new http.SimpleHTTPBuilder(this,params.launchconfig)
                    def httpObj2 =new http.SimpleHttpBuilder(this,params.targetgroup)
                    httpObj.initialconfig()

                    httpObj.sendRequest()

                }
            }
        }


        // stage('Build') {
        //     steps {
        //         script {
        //             def buildObj = new build.Build(this,params)
        //             buildObj.checkout()
        //         }
        //     }
        // }
    }
}
