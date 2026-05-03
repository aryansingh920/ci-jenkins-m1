// ci/groovy/logic.groovy
def _version = ""

def generateVersion(String branch) {
    def timestamp = new Date().format("yyyyMMdd-HHmm")
    def shortHash = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    _version = "${branch}-${timestamp}-${shortHash}"
    return _version
}

def getVersion() {
    return _version
}

def logStage(String message) {
    echo "***************************"
    echo "ENTERED STAGE: ${message}"
    echo "***************************"
}

return this
