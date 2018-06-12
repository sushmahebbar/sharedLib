package org.k9.http

@Grab('io.github.http-builder-ng:http-builder-ng-core:1.0.3')
// @Grab('org.jsoup:jsoup:1.9.2')

import static groovyx.net.http.HttpBuilder.configure
import static groovyx.net.http.ContentTypes.JSON
import groovyx.net.http.*
import static groovy.json.JsonOutput.prettyPrint
// import org.jsoup.nodes.Document

class SimpleHTTPBuilder implements Serializable  {
    def config
    def script

    SimpleHTTPBuilder(script, config) {
        this.script = script
        this.config = config
    }

 @NonCPS
    def sendRequest() {
        def url = config.httpParams.url.toString()
        def path = config.httpParams.path.toString()
        def method = config.httpParams.method.toLowerCase()
        this.script.echo "http send request ${config}, ${url}, ${path}, ${method}"
        def funcOutput = [:]
        switch (config['httpParams']['method']) {
            case "GET":
                try {
                    // def result = configure {
                    //                     request.uri = url
                    //             }."${method}"(){
                    //                     request.uri.path = path
                    //             }

                    def result = configure {
                                        request.uri = url
                                }."${method}"()
                    this.script.echo "(In SimpleHTTPBuilder) :: ${result} --> ${result.getClass()}"
                    if ("${result.getClass()}" == "class groovy.json.internal.LazyMap"){
                        result.each { resKey, resVal ->
                            funcOutput[resKey] = resVal
                        }
                        this.script.echo "(In SimpleHTTPBuilder Func) :: ${funcOutput.getClass()}"
                        return funcOutput
                    }
                    this.script.echo "(In SimpleHTTPBuilder Res) :: ${result.getClass()}"
                    return result
                } catch(Exception ex) {
                    this.script.echo "Exception ${ex}"
                    return ['response': 'error']
                }
            case "POST":
            case "PUT":
            case "DELETE":
            def test = [:]
                try {
                    def result = configure {
                                    request.uri = url
                                    request.contentType = JSON[0]
                                }."${method}"() {
                                    request.uri.path = path
                                    request.body = config.jsonBody
                                }
                    this.script.echo "(In SimpleHTTPBuilder) :: ${result} --> ${result.getClass()}"
                    if ("${result.getClass()}" == "class groovy.json.internal.LazyMap"){
                        result.each { resKey, resVal ->
                            funcOutput[resKey] = resVal
                        }
                        this.script.echo "(In SimpleHTTPBuilder Func) :: ${funcOutput.getClass()}"
                        return funcOutput
                    }
                    this.script.echo "(In SimpleHTTPBuilder Res) :: ${result.getClass()}"
                    return result
                } catch(Exception ex) {
                    this.script.echo "Exception ${ex}"
                    return ['response': 'error']
                }
        }
    }
}




// ********************************************* //
// POST, PUT, DELETE
// config = [jsonBody: [id: '234545', label: 'something interesting'],  
            //  httpParams: [
            //         method: "POST",  
            //         url: "http://httpbin.org", 
            //         path: "/post"]
            // ]
// GET
// config = [ httpParams: [method: "GET",  url: "http://httpbin.org", path: "/"] ]
// ********************************************* //


//****************** Jenkinsfile ****************//
// @Library('aws') _
// import org.k9.*

// pipeline {
//     agent any
//     stages {
//         stage('Create-Target-Group') {
//             steps {
//                 script {
//                     // GET using cURL
//                     def config = [httpParams: [method: "GET",  url: "http://httpbin.org", path: "/"] ]

//                     def obj = new http.SimpleHTTPBuilder(this,config)
//                     output = obj.sendRequest()
//                     echo "GET Output: ${output}"
//                 }
//             }
//         }
//     }
// }


// def param = [id: '234545', label: 'something interesting']
// def curlParams = [method: "POST",  url: "http://httpbin.org", path: "/post"]
// def config = [jsonBody: param, httpParams: curlParams]                       //>>>>>>>>>>>>>>>>>>> for POST, PUT, DELETE
// // def config = [httpParams: curlParams]                                     //>>>>>>>>>>>>>>>>>>> for GET