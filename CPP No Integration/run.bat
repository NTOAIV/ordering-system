
@echo off
cd /d "%~dp0"
setlocal

echo ========================================
echo   Restaurant Ordering System - C++
echo ========================================
echo.

echo Checking C++ compiler...

where g++ >nul 2>&1

if errorlevel 1 (
    echo.
    echo ERROR: C++ compiler not found!
    echo.
    echo You need to install a C++ compiler to run this project.
    echo.
    echo Recommended: Install MinGW-w64 with GCC.
    echo Download: https://www.mingw-w64.org/
    echo.
    echo After installation, add the MinGW bin folder to PATH.
    echo Example: C:\mingw64\bin
    echo.
    echo Restart your terminal after installation.
    pause
    exit /b 1
)

echo C++ compiler found!
echo.

echo Compiling project...

set "TEMP_EXE=%TEMP%\RestaurantOrderingSystem.exe"

g++ -std=c++17 -Wall -Wextra -I .\src .\src\*.cpp -o "%TEMP_EXE%"

if errorlevel 1 (
    echo.
    echo Compilation failed!
    pause
    exit /b 1
)

echo.
echo Compilation successful!
echo Starting application...
echo.

:: Run application
"%TEMP_EXE%"

:: Delete temporary executable
del "%TEMP_EXE%" 2>nul

echo.
echo Application closed.
pause

endlocal

