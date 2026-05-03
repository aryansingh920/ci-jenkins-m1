import subprocess
import sys
import json


def run(cmd, **kwargs):
    return subprocess.run(cmd, **kwargs)


def scan_dependencies():
    print("=== Dependency Vulnerability Scan ===")

    # Step 1: Install pip itself via apt (Jenkins container is Debian/Ubuntu based)
    print("Installing pip via apt...")
    run(["apt-get", "install", "-y", "-q", "python3-pip"], check=True)

    # Step 2: Install pip-audit
    print("Installing pip-audit...")
    run(["python3", "-m", "pip", "install", "pip-audit",
        "--quiet", "--break-system-packages"], check=True)

    # Step 3: Run the scan
    result = run(
        ["python3", "-m", "pip_audit",
         "--requirement", "app/requirements.txt",
         "--format", "json",
         "--skip-editable"],
        capture_output=True,
        text=True
    )

    if result.returncode == 0:
        print("SUCCESS: No known vulnerabilities found.")
        return

    try:
        data = json.loads(result.stdout)
        vulns = [d for d in data.get("dependencies", []) if d.get("vulns")]
        if not vulns:
            print("SUCCESS: No known vulnerabilities found.")
            return

        print(f"FOUND {len(vulns)} vulnerable package(s):")
        for dep in vulns:
            for v in dep["vulns"]:
                fix = v.get("fix_versions", ["unfixed"])
                print(
                    f"  - {dep['name']} {dep['version']}: {v['id']} (fix: {fix})")
        sys.exit(1)

    except json.JSONDecodeError:
        print(result.stdout)
        sys.exit(1)


if __name__ == "__main__":
    scan_dependencies()
