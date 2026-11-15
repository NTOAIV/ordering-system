#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

echo "============================================"
echo " JAVA ORDERING SYSTEM (NO INTEGRATION)"
echo "============================================"

for tool in javac java; do
    if ! command -v "$tool" >/dev/null 2>&1; then
        echo "[error] $tool was not found on PATH." >&2
        exit 1
    fi
done

mkdir -p out

echo "[1/2] Compiling Java application..."
javac -d out src/*.java

echo "[2/2] Running Java application..."
java -cp out ordering.Main

echo
echo "Done."
