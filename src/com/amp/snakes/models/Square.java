// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 7th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes.models;

import com.amp.snakes.enums.Color;
import com.amp.snakes.enums.SquareType;

import java.util.ArrayList;

public class Square {
    private static final Color SNAKE_COLOR = Color.RED;
    private static final Color LADDER_COLOR = Color.GREEN;
    private static final Color MIXED_COLOR = Color.YELLOW;
    private static final Color NEUTRAL_COLOR = Color.RESET;

    private final int LINKED_SQUARE;
    private final int NB;
    private final SquareType LINKED_TYPE;
    private final Color COLOR;

    private final ArrayList<Player> CURRENT_PLAYERS = new ArrayList<>(2);

    /* CONSTRUCTORS */

    public Square(){
        this(-1, 0, SquareType.None);
    }

    public Square(int LINKED_SQUARE, int NB, SquareType LINKED_TYPE)
    {
        this.LINKED_SQUARE = LINKED_SQUARE;
        this.NB = NB;
        this.LINKED_TYPE = LINKED_TYPE;
        switch (LINKED_TYPE) {
            case Snake -> COLOR = SNAKE_COLOR;
            case Ladder -> COLOR = LADDER_COLOR;
            default -> COLOR = NEUTRAL_COLOR;
        }
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

    public ArrayList<Player> getCURRENT_PLAYERS() {
        return CURRENT_PLAYERS;
    }

    public Color getCOLOR() {
        return COLOR;
    }

    public static Color getSNAKE_COLOR() {
        return SNAKE_COLOR;
    }

    public static Color getLADDER_COLOR() {
        return LADDER_COLOR;
    }

    public static Color getMIXED_COLOR() {
        return MIXED_COLOR;
    }

    public static Color getNEUTRAL_COLOR() {
        return NEUTRAL_COLOR;
    }

    public void addCurrentPlayer(Player currentPlayer) {
        this.CURRENT_PLAYERS.add(currentPlayer);
    }

    public void removeCurrentPlayer(Player currentPlayer) {
        this.CURRENT_PLAYERS.remove(currentPlayer);
    }

    /* PUBLIC METHODS */

    public void reset()
    {
        CURRENT_PLAYERS.clear();
    }
}
