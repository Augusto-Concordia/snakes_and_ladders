// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes;

import com.amp.snakes.enums.SquareType;
import com.amp.snakes.utility.ConfigHandler;

import java.util.Objects;
import java.util.Random;

public class LadderAndSnake {
    private final Square[][] GAME_BOARD;
    private final int BOARD_SIZE;
    private final int NB_PLAYERS;

    /* ACCESSORS & MUTATORS */

    public Square[][] getGAME_BOARD() {
        return GAME_BOARD;
    }

    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    public int getNB_PLAYERS() {
        return NB_PLAYERS;
    }

    /* CONSTRUCTORS */

    public LadderAndSnake(int BOARD_SIZE, int NB_PLAYERS) {
        this(BOARD_SIZE, ConfigHandler.getBoardConfig(null), NB_PLAYERS);
    }

    public LadderAndSnake(LadderAndSnake ladderAndSnake) {
        this(ladderAndSnake.BOARD_SIZE, ladderAndSnake.GAME_BOARD, ladderAndSnake.NB_PLAYERS);
    }

    private LadderAndSnake(int BOARD_SIZE, Square[][] GAME_BOARD, int NB_PLAYERS) {
        if (GAME_BOARD == null) {
            this.BOARD_SIZE = -1;
            this.NB_PLAYERS = -1;
            this.GAME_BOARD = null;

            System.out.println("Error: Invalid Board Config File! Current game is borked and will close...");
            System.exit(-1);
            return;
        }

        this.BOARD_SIZE = BOARD_SIZE;
        this.NB_PLAYERS = NB_PLAYERS;
        this.GAME_BOARD = Objects.requireNonNull(GAME_BOARD).clone();
    }

    /* PUBLIC METHODS */

    public void play() {

    }

    public int flipDice() {
        return (new Random()).nextInt(5) + 1;
    }

    public void printBoard() {
        int width = 6;
        int height = 4;
        int skip = 0;
        for (int row = BOARD_SIZE * height; row >= 0; row--) {
            for (int column = 0; column <= BOARD_SIZE * width; column++) {

                //if (row == BOARD_SIZE * height - 2 && column == BOARD_SIZE * width - 3) System.out.print("Winner winner chicken dinner!"); //todo lul just remove this hehe

                if (row == BOARD_SIZE * height) { //Top line

                    if (column == 0) System.out.print("╔"); //Left column
                    else if (column == BOARD_SIZE * width) System.out.print("╗"); //Right column
                    else if (column % width == 0) System.out.print("╤"); //Middle separator
                    else System.out.print("═"); //Inside a square

                } else if (row == 0) { //Bottom line

                    if (column == 0) System.out.print("╚"); //Left column
                    else if (column == BOARD_SIZE * width) System.out.print("╝"); //Right column
                    else if (column % width == 0) System.out.print("╧"); //Middle separator
                    else System.out.print("═"); //Inside a square

                } else if (row % height == 0) { //Horizontal separator

                    if (column == 0) System.out.print("╟"); //Left column
                    else if (column == BOARD_SIZE * width) System.out.print("╢"); //Right column
                    else if (column % width == 0) System.out.print("┼"); //Middle separator
                    else System.out.print("─"); //Inside a square

                } else { //Inside a square
                    if (column == 0 || column == BOARD_SIZE * width) { //Left or right column
                        System.out.print("║");
                    } else if (column % width == 0) { //Middle separator
                        System.out.print("│");
                    } else { //Information inside a square
                        if ((row - 1) % height == 0 && (column - 1) % width == 0) { //Bottom left corner of a square
                            System.out.printf("%1$-3d", getGAME_BOARD()[row / height][column / width].getNB());
                            skip = 2;
                        } else if ((row + 1) % height == 0 && (column - 1) % width == 0) { //top left corner of a square
                            switch (getGAME_BOARD()[row / height][column / width].getLINKED_TYPE().toString()) {
                                case ("None") -> System.out.print("     ");
                                case ("Snake") -> System.out.print("s>" + String.format("%1$-3d", getGAME_BOARD()[row / height][column / width].getLINKED_SQUARE()));
                                case ("Ladder") -> System.out.print("l>" + String.format("%1$-3d", getGAME_BOARD()[row / height][column / width].getLINKED_SQUARE()));
                            }
                            skip = 4;
                        } else if (skip > 0) {
                            skip--;
                        } else {
                            System.out.print(" ");
                        }
                    }
                }
            }
            System.out.println();
        }
    }

//╔═╤═╗
//║ │ ║
//╟─┼─╢
//║ │ ║
//╚═╧═╝
}