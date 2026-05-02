import sys


def check_dockerfile(filePath):
    print("--- [Python] Auditing Dockerfile Security ---")
    try:
        with open(filePath, 'r') as f:
            content = f.read()
            if "USER" not in content.upper():
                print("CRITICAL: Dockerfile does not specify a non-root USER!")
                sys.exit(1)  # This kills the Jenkins stage
            print("SUCCESS: Dockerfile passed security audit.")
    except FileNotFoundError:
        print("ERROR: Dockerfile missing.")
        sys.exit(1)


if __name__ == "__main__":
    check_dockerfile(filePath="app/Dockerfile")
