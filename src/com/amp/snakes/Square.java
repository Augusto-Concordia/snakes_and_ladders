// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (XXXXXXXX)
// -----------------------------------------------------

package com.amp.snakes;

import com.amp.snakes.enums.SquareType;

public class Square {
    private final int linkedSquare;
    private final SquareType linkedType;

    public Square(){
        this(-1, SquareType.None);
    }

    public Square(int linkedSquare, SquareType linkedType)
    {
        this.linkedSquare = linkedSquare;
        this.linkedType = linkedType;
    }
}
