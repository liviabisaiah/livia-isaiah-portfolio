/*
Livia Isaiah COP4020 
li872007

book.h
header file for the Book class, which represents a book in the inventory.
The class includes private attributes for the name and price of the book, 
public constructors, a destructor, getters and setters for the attributes, 
and virtual functions to calculate the real price of the book and to 
return a string representation of the book.
*/

#ifndef BOOK_H
#define BOOK_H

#include <string>

class Book {
private:
    std::string name;
    double price;

public:
    // Constructors
    Book();
    Book(std::string btitle, double gprice);
    Book(std::string line);

    // Destructor
    virtual ~Book();

    // Getters
    std::string getName();
    double getPrice();

    // Setters
    void setName(const std::string n);
    void setPrice(double p);

    // Virtual functions
    virtual double getRealPrice();
    virtual std::string str();
};

#endif