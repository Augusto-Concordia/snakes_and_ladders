// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (XXXXXXXX)
// -----------------------------------------------------

package com.amp.snakes;

import com.amp.snakes.utility.ConfigHandler;

import java.util.Objects;
import java.util.Random;

public class SnakesAndLadders {
    private final Square[][] gameBoard;
    private final int boardSize;
    private final int numberOfPlayers;

    /* ACCESSORS & MUTATORS */
    public Square[][] getGameBoard() {
        return gameBoard;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /* CONSTRUCTORS */

    public SnakesAndLadders(int boardSize, int numberOfPlayers) {
        this(boardSize, ConfigHandler.getBoardConfig(null, boardSize), numberOfPlayers);
    }

    public SnakesAndLadders(SnakesAndLadders snakesAndLadders) {
        this(snakesAndLadders.boardSize, snakesAndLadders.gameBoard, snakesAndLadders.numberOfPlayers);
    }

    private SnakesAndLadders(int boardSize, Square[][] gameBoard, int numberOfPlayers) {
        if (gameBoard == null) {
            this.boardSize = -1;
            this.numberOfPlayers = -1;
            this.gameBoard = null;

            System.out.println("Error: Invalid Board Config File! Current game is borked and will close...");
            System.exit(-1);
            return;
        }

        this.boardSize = boardSize;
        this.numberOfPlayers = numberOfPlayers;
        this.gameBoard = Objects.requireNonNull(gameBoard).clone();
    }

    /* PUBLIC METHODS */
    public void play()
    {

    }

    public int flipDice()
    {
        return (new Random()).nextInt(5) + 1;
    }
}
