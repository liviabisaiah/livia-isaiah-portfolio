/*
Livia Isaiah COP4020 
li872007

inventory.h
header file for the Inventory class, which manages a collection of books.
The class includes a constructor that reads book data from a file,
 a destructor, and functions to add books, check if the inventory is empty,
 return a string representation of the inventory, calculate the total price of the books,
 search for books by name, and display a menu for user interaction.
*/

#ifndef INVENTORY_H
#define INVENTORY_H

#include <vector>
#include <memory>
#include <string>
#include "book.h"

class Inventory {
private:
    std::vector<std::unique_ptr<Book>> vec;

public:
    Inventory(const std::string fname);
    ~Inventory();

    void readData(std::string fname);
    void add(std::unique_ptr<Book> book);

    bool isEmpty();
    std::string str();
    double getRealTotal();

    void searchBook(const std::string bname);
    void menu();
};

#endif