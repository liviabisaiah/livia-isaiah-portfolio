/*
Livia Isaiah COP4020 
li872007

spellbook.cpp
implementation of the SpellBook class, which inherits from the Book class. 
The class includes constructors, a destructor, a getter for the canTalk attribute, 
and overridden virtual functions to calculate the real price of the spellbook 
and to return a string representation of the spellbook.
*/


#include "spellBook.h"
#include <sstream>
#include <iomanip>

// Constructors
SpellBook::SpellBook() {
    canTalk=true;

}

SpellBook::SpellBook(std::string gname, double gprice, bool cantalkparam) {
    setName(gname);
    setPrice(gprice);   
    canTalk = cantalkparam;

}

SpellBook::SpellBook(std::string line) {
    // Parse the line to extract the name, price, and canTalk values
    std::stringstream ss(line);
    std::string type;
    std::string tempname;
    double tempprice;
    std:: string tempcanTalk;
    ss >> type >> tempname >> tempprice >> tempcanTalk;
    setName(tempname);
    setPrice(tempprice);
    canTalk = (tempcanTalk == "1" || tempcanTalk == "true");
}

// Destructor
SpellBook::~SpellBook() {
}

// Getter
bool SpellBook::getCanTalk() {
    return canTalk;
}

// Overridden functions
double SpellBook::getRealPrice() {
    if (canTalk) {
        return getPrice() * 1.1;
    } else {
        return getPrice();
    }
}

std::string SpellBook::str() {
    std::stringstream ss;
    ss << std::fixed << std::setprecision(6) ;
    if(canTalk) {
        ss << "Speaking Book:" << getName() << " price:"<< getRealPrice()<< std::endl;
    } else {
        ss << "Spell Book:" << getName() << " price:"<< getRealPrice()<< std::endl;
    }
    return ss.str();
}