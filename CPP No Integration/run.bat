
@echo off
cd /d "%~dp0"

echo ========================================
echo   Restaurant Ordering System - C++
echo ========================================
echo.
echo Compiling project...

g++ -std=c++17 -Wall -Wextra -I .\src .\src\*.cpp -o RestaurantOrderingSystem.exe

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

RestaurantOrderingSystem.exe

echo.
echo Application closed.
pause
