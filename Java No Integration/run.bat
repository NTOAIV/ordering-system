@echo off
setlocal
cd /d "%~dp0"

echo ============================================
echo  JAVA ORDERING SYSTEM (NO INTEGRATION)
echo ============================================

where javac >nul 2>nul
if errorlevel 1 (
    echo [error] javac was not found. Install a JDK and add its bin folder to PATH.
    exit /b 1
)

where java >nul 2>nul
if errorlevel 1 (
    echo [error] java was not found. Install a JDK and add its bin folder to PATH.
    exit /b 1
)

if not exist out mkdir out

echo [1/2] Compiling Java application...
javac -d out src\*.java
if errorlevel 1 (
    echo [error] Java compilation failed.
    exit /b 1
)

echo [2/2] Running Java application...
java -cp out ordering.Main
if errorlevel 1 (
    echo [error] Java runtime failed.
    exit /b 1
)

echo.
echo Done.
