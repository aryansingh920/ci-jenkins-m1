def publishReports(String reportsRepoUrl, String credentialsId, String appVersion) {
    echo "=== Publishing Reports to ${reportsRepoUrl} ==="

    // Configure git identity for the commit
    sh """
        git config --global user.email "jenkins@ci.local"
        git config --global user.name "Jenkins CI"
    """

    // Clone the reports repo into a subdirectory
    withCredentials([usernamePassword(
        credentialsId: credentialsId,
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_TOKEN'
    )]) {
        sh """
            # Clone reports repo
            rm -rf reports-repo
            git clone https://\${GIT_USER}:\${GIT_TOKEN}@${reportsRepoUrl.replace('https://', '')} reports-repo

            # Create versioned directory for this build
            mkdir -p reports-repo/reports/${appVersion}

            # Copy all report artifacts
            [ -f trivy-report.txt ]  && cp trivy-report.txt  reports-repo/reports/${appVersion}/
            [ -f build-report.txt ]  && cp build-report.txt  reports-repo/reports/${appVersion}/

            # Write a small index entry
            echo "${appVersion} - \$(date '+%Y-%m-%d %H:%M:%S')" >> reports-repo/reports/index.txt

            # Commit and push
            cd reports-repo
            git add .
            git diff --cached --quiet && echo "No changes to publish" && exit 0
            git commit -m "CI: publish reports for ${appVersion}"
            git push
        """
    }

    echo "Reports published for ${appVersion}"
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
