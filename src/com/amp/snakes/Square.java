// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes;

import com.amp.snakes.enums.SquareType;

public class Square {
    private final int LINKED_SQUARE;
    private final int NB;
    private final SquareType LINKED_TYPE;


    public Square(){
        this(-1, 0, SquareType.None);
    }

    public Square(int LINKED_SQUARE, int NB, SquareType LINKED_TYPE)
    {
        this.LINKED_SQUARE = LINKED_SQUARE;
        this.NB = NB;
        this.LINKED_TYPE = LINKED_TYPE;
    }

    /* ACCESSORS & MUTATORS */

    public int getLINKED_SQUARE() {
        return LINKED_SQUARE;
    }

    public int getNB() {
        return NB;
    }

    public SquareType getLINKED_TYPE() {
        return LINKED_TYPE;
    }
}
