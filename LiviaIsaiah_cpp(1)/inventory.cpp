/*
Livia Isaiah COP4020 
li872007

inventory.cpp
implementation of the Inventory class.
The class includes a constructor that reads book data from a file, 
a destructor, and functions to add books, check if the inventory is empty,
 return a string representation of the inventory, calculate the total price of the books, 
 search for books by name, and display a menu for user interaction.
*/

#include "inventory.h"
#include "spellbook.h"
#include "book.h"
#include <fstream>
#include <sstream>
#include <iostream>
#include <iomanip>

// Constructor
Inventory::Inventory(std::string fname) {
    readData(fname);
}

// Destructor
Inventory::~Inventory() {
}

// Core functions
void Inventory::readData(std::string fname) {
    std::ifstream infile(fname);
    if (!infile) {
        std::cerr << "Error opening file: " << fname << std::endl;
        return;
    }

    std::string line;
    while (std::getline(infile, line)) {
        if (line.empty()) continue; // Skip empty lines
        char type=line[0]; // Get the first character to determine the type of book

        if (type == 'b') {
            vec.push_back(std::make_unique<Book>(line));
        } else if (type == 's') {
            vec.push_back(std::make_unique<SpellBook>(line));
        } else {
            std::cerr << "Unknown book type: " << type << std::endl;
        }
    }
}

void Inventory::add(std::unique_ptr<Book> book) {
    vec.push_back(std::move(book));
}

bool Inventory::isEmpty() {
    return vec.empty();
}

std::string Inventory::str() {
    std::cout<<"The book/s in the inventory is/are"<<std::endl;
    std::stringstream ss;
    for (auto &b : vec) {
        ss << b->str();
    }
    ss << std::endl;
    return ss.str();

    
}

double Inventory::getRealTotal() {
    double total = 0.0;
    for (auto &b : vec) {
        total += b->getRealPrice();
    }
    std::cout << std::fixed << std::setprecision(6);
    return total;
}

void Inventory::searchBook(std::string bname) {
    std::cout<<"Searched book name: "<<bname<<std::endl;
    bool found = false;

    for(auto &b : vec){
        if (b->getName().find(bname) != std::string::npos) {
            std::cout << b->str();
            found = true;
        }
    }   
    if (!found) {
        std::cout << "No result found" << std::endl;
    }

std::cout << std::endl;
}

void Inventory::menu() {
    if (isEmpty()) return;

    std::string input;

    while (true) {
        std::cout << "Enter option search BOOKNAME/all/total/done" << std::endl;
        std::getline(std::cin, input);

        if (input == "done") {
            break;
        }
        else if (input == "all") {
            std::cout << str();
        }
        else if (input == "total") {
            std::cout << "Total price of the books is " << getRealTotal()<< std::endl;
            std::cout << std::endl;
        }
        else if (input.rfind("search ", 0) == 0) {
            std::string bookName = input.substr(7);
            searchBook(bookName);
        }
        else {
            std::cout << "Invalid option" << std::endl;
            std::cout << std::endl;
        }
    }
}