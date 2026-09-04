/*
Livia Isaiah COP4020 
li872007

spellbook.h
header file for the SpellBook class, which inherits from the Book class.
The class includes private attributes for whether the spellbook can talk, 
public constructors, a destructor, a getter for the canTalk attribute,
 and overridden virtual functions to calculate the real price of the 
 spellbook and to return a string representation of the spellbook.  
*/

#ifndef SPELLBOOK_H
#define SPELLBOOK_H

#include "book.h"
#include <string>

class SpellBook : public Book {
private:
    bool canTalk;

public:
    // Constructors
    SpellBook();
    SpellBook(std::string gname, double gprice, bool cantalkparam);
    SpellBook(std::string line);

    // Destructor
    virtual ~SpellBook();

    // Getter
    bool getCanTalk();

    // Overridden virtual functions
    double getRealPrice() override;
    std::string str() override;
};

#endif