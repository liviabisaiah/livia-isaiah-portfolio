/*
Livia Isaiah COP4020 
li872007

main.cpp
main file that contains the main function to run the program.
 It prompts the user for a filename, creates an Inventory object using the provided filename, 
 and then calls the menu function to interact with the inventory.
*/

#include <iostream>
#include <string>
#include "inventory.h"

int main() {
    std::cout << "Enter filename" << std::endl;
    std::string fname;
    std::getline(std::cin, fname);
    Inventory inv(fname);
    inv.menu();

    return 0;
}