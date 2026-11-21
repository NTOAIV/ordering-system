
#!/usr/bin/env bash

cd "$(dirname "$0")" || exit 1

echo "========================================"
echo "  Restaurant Ordering System - C++"
echo "========================================"
echo
echo "Compiling project..."

g++ -std=c++17 -Wall -Wextra -I ./src ./src/*.cpp -o RestaurantOrderingSystem.exe

if [ $? -ne 0 ]; then
    echo
    echo "Compilation failed!"
    exit 1
fi

echo
echo "Compilation successful!"
echo "Starting application..."
echo

./RestaurantOrderingSystem.exe
