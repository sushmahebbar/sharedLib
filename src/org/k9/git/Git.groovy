package org.k9.git

class Git implements Serializable {
    def script
    def config

    Git(def script, def config) {
        this.script = script
        this.config = config
    }

    def LaunchConfiguration() {
       // this.script.echo "Inside checkout"
        //this.script.git "${this.config.giturl}"
        //this.script.echo "Global var : ${this.script.utils.dummyVar}"
        
    }
}