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

def generateReport(String version, String branch, boolean testsPassed, boolean auditPassed) {
    def report = """
=== BUILD REPORT ===
Version    : ${version}
Branch     : ${branch}
Built At   : ${new Date().format("yyyy-MM-dd HH:mm:ss")}
Tests      : ${testsPassed ? "PASSED" : "FAILED"}
Audit      : ${auditPassed ? "PASSED" : "FAILED"}
====================
"""
    writeFile file: 'build-report.txt', text: report
    echo report
    return report
}

return this
