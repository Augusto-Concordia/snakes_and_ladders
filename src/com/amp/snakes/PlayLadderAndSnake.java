// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
// Question: N/A
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes;

import com.amp.snakes.utility.ConfigHandler;

public class PlayLadderAndSnake {

    public static void main(String[] args) {
        LadderAndSnake game = new LadderAndSnake(10, 2);
        game.printBoard();
    }
}
