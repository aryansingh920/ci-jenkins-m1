// groovy/logic.groovy

def generateVersion(String branch) {
    def timestamp = new Date().format("yyyyMMdd-HHmm")
    def shortHash = sh(script: "git rev-parse --short HEAD", returnStdout: true).trim()
    return "${branch}-${timestamp}-${shortHash}"
}

def logStage(String stageName) {
    echo "***************************"
    echo "ENTERED STAGE: ${stageName}"
    echo "***************************"
}

return this; // Required to 'load' this into Jenkins
