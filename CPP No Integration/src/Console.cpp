#include "Console.h"
#include <iostream>
#include <limits>
#include <cstdlib>

using namespace std;

int Console::getInt(const string& prompt) {
    int value;

    while (true) {
        cout << prompt;

        if (cin >> value) {
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            return value;
        }

        cout << "Invalid input. Please enter a number.\n";

        cin.clear();
        cin.ignore(numeric_limits<streamsize>::max(), '\n');
    }
}

string Console::getString(const string& prompt) {
    string value;

    cout << prompt;
    getline(cin, value);

    return value;
}

void Console::pause() {
    cout << "\nPress Enter to continue...";
    cin.get();
}

void Console::clearScreen() {
    system("cls");
}

