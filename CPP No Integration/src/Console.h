#ifndef CONSOLE_H
#define CONSOLE_H

#include <string>
using namespace std;

class Console {
public:
    static int getInt(const string& prompt);
    static string getString(const string& prompt);
    static void pause();
    static void clearScreen();
};

#endif
