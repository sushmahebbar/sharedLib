@Library('test@master') _
        
import org.k9.*

pipeline {
    agent any
    stages {
        stage('create resource') {
            steps {
                script {
                    def config = [
                            launchconfig: [
                                    "jsonBody": [
                                        "imageId": "ami-f2d3638a",
                                        "instanceType": "t2.micro",
                                        "launchConfigurationName": "http.buildNo",
                                        "securityGroups": ["sg-879965f8"],
                                        "userData": "https://s3-us-west-2.amazonaws.com/hudsonbay-test/aws/dev/script/blue-green/R1/scriptViaGroovy.sh",
                                        "keyName": "HudsonBay-V",
                                        "iamInstanceProfile": "ec2-s3-RO-role"
                                    ],
                                    httpParams: [
                                        url: "http://localhost:4321",
                                        path: "/v1/launchconfig",
                                        method: "POST"
                                    ]
                                ],
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
                                ],
                                elb :[
                                   jsonBody: [
                                       "name":"appELB",
                                       "securityGroups": ["sg-879965f8"],
                                       "subnets":["subnet-4f628736", "subnet-4787521d", "subnet-b0dfdbf8"]
                                        ],
                                    httpParams:[
                                         method: "POST",
                                         url: "http://localhost:4321",
                                         path: "/v1/loadbalancer"
                                    ]
                            ],
                                elbListener: [
                                     jsonBody: [
                                     "defaultActions": [
                                    [
                                        "targetGroupArn": "arn:aws:elasticloadbalancing:us-west-2:702599048949:targetgroup/cool/20582d5d0f31e39e"
                                    ]
                                ],
                                "loadBalancerArn":"arn:aws:elasticloadbalancing:us-west-2:702599048949:loadbalancer/app/cool/77f57bdf01ad5db1",
                                "port": "80",
                                "protocol":"HTTP"
                            ],
                            httpParams:[
                                method: "PUT",
                                url: "http://localhost:4321",
                                path: "/v1/loadbalancer"
                            ]
                        ],
                        asg: [
                            jsonBody: [
                                    "autoScalingGroupName": "my-auto-scaling-group",
                                    "launchConfigurationName": "my-launch-config",
                                    "desiredCapacity": "1",
                                    "maxSize": "2",
                                    "minSize": "1",
                                    "vpcZoneIdentifier": "subnet-4f628736",
                                    "targetGroupARNs":["arn:aws:elasticloadbalancing:us-west-2:702599048949:targetgroup/tg-1/fd89f1ccef7aff8c"]
                                ],
                            httpParams:[
                                 method: "POST",
                                 url: "http://localhost:4321",
                                 path: "/v1/autoscale"
                            ]
                        ],

                            ]
                
                   // params.launchconfig.jsonBody["launchConfigurationName"]="Lc"+env.BUILD_NUMBER
                    //def httpObj = new http.SimpleHTTPBuilder(this,params)
                    //def httpObj2 =new http.Saws.deploy()impleHttpBuilder(this,params.targetgroup)
                    //httpObj.initialconfig()
                    def obj= new aws.ami.AMIDeployment(this,config)
                    obj.deploy()
                    //aws.CreateLaunchconfig()
                    //aws.CreateTargetgroup()
                    //httpObj.sendRequest()
                   // httpObj2.sendRequest()

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
