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
