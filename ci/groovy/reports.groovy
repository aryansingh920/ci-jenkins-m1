def publishReports(String appVersion, String gitUser, String gitToken) {
    echo "=== Publishing Reports to ci-jenkins-m1/reports ==="

    sh """
        git config --global user.email "jenkins@ci.local"
        git config --global user.name "Jenkins CI"

        # Clone the repo into a temp folder
        rm -rf publish-tmp
        git clone https://${gitUser}:${gitToken}@github.com/aryansingh920/ci-jenkins-m1.git publish-tmp

        # Create versioned reports directory
        mkdir -p publish-tmp/reports/${appVersion}

        # Copy reports if they exist
        [ -f trivy-report.txt ] && cp trivy-report.txt publish-tmp/reports/${appVersion}/
        [ -f build-report.txt ] && cp build-report.txt publish-tmp/reports/${appVersion}/

        # Append to index
        echo "${appVersion} - \$(date '+%Y-%m-%d %H:%M:%S')" >> publish-tmp/reports/index.txt

        # Commit and push
        cd publish-tmp
        git add reports/
        git diff --cached --quiet && echo "No new reports to publish." && exit 0
        git commit -m "ci: publish reports for ${appVersion}"
        git push

        cd ..
        rm -rf publish-tmp
    """

    echo "=== Reports published under reports/${appVersion} ==="
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
