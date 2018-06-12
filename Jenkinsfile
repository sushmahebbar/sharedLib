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
                                        "launchConfigurationName": "testLibLC"
                                        "securityGroups": ["sg-879965f8"],
                                        "userData": "https://s3-us-west-2.amazonaws.com/hudsonbay-test/dev/scripts/scriptViaGroovy.sh",
                                        "keyName": "HudsonBay-V",
                                        "iamInstanceProfile": "ec2-s3-RO-role"
                                    ],
                                    httpParams: [
                                        url: "http://localhost:4321",
                                        path: "/v1/launchconfig",
                                        method: "POST"
                                    ]
                                ]
                            ]
                      
                    def httpObj = new http.SimpleHTTPBuilder(this,params.launchconfig)
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
