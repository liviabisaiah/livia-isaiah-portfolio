/*
Livia Isaiah COP4020 
li872007

book.cpp
implementation of the Book class.
The class includes constructors, a destructor, getters and setters for the name and price attributes,
and overridden virtual functions to calculate the real price of the book and to return a string representation of the book.
*/


#include "book.h"
#include <sstream>
#include <iomanip>

// Constructors
Book::Book() {
    name="";
    price=100;
}

Book::Book(std::string btitle, double gprice) {
    setName(btitle);
    setPrice(gprice);   
}

Book::Book(std::string line) {
    // Parse the line to extract the name and price
    std::stringstream ss(line);
    //temp variables to hold the parsed values
    std:: string type;
    std:: string tempname;
    double tempprice;
    ss >> type >> tempname >> tempprice;
    name = tempname;
    price = tempprice;

}

// Destructor
Book::~Book() {

}

// Getters
std::string Book::getName() {
    return name;
}

double Book::getPrice() {
    return price;
}

// Setters
void Book::setName(const std::string n) {
    name = n;
}

void Book::setPrice(double p) {
    price = p;
}

// Virtual functions
double Book::getRealPrice() {
    return price*0.8;
}

std::string Book::str() {
    std::stringstream ss;
    ss << std::fixed << std::setprecision(6) ;
    ss << "Literature Book:" << name << " price:"<< getRealPrice()<<std::endl;
    return ss.str();

}