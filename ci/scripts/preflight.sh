#!/bin/bash
set -e   # exit immediately on any error

echo "=== Pre-flight Check ==="

# Check Python version is 3.9+
PYTHON_VERSION=$(python3 -c 'import sys; print(sys.version_info.minor)')
if [ "$PYTHON_VERSION" -lt 9 ]; then
    echo "FAIL: Python 3.9+ required, found 3.${PYTHON_VERSION}"
    exit 1
fi
echo "OK: Python version acceptable"

# Check requirements.txt exists
if [ ! -f "app/requirements.txt" ]; then
    echo "FAIL: app/requirements.txt missing"
    exit 1
fi
echo "OK: requirements.txt found"

# Check no plaintext secrets in repo
if grep -r "password\s*=\s*['\"][^'\"]*['\"]" app/ --include="*.py"; then
    echo "FAIL: Possible hardcoded password found"
    exit 1
fi
echo "OK: No hardcoded secrets detected"

echo "=== Pre-flight PASSED ==="
