import subprocess
import sys
import json


def scan_dependencies():
    print("=== Dependency Vulnerability Scan ===")

    # python3 -m pip always works regardless of how pip is symlinked
    subprocess.run(
        ["python3", "-m", "pip", "install", "pip-audit", "--quiet"],
        check=True
    )

    result = subprocess.run(
        ["python3", "-m", "pip_audit",
         "--requirement", "app/requirements.txt",
         "--format", "json", "--skip-editable"],
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
