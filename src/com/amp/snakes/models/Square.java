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

/**
 * Represents a game square.
 */
public class Square {
    private static final Color SNAKE_COLOR = Color.RED; // The color of a square containing a snake.
    private static final Color LADDER_COLOR = Color.GREEN; // The color of a square containing a ladder.
    private static final Color MIXED_COLOR = Color.YELLOW; // The color of a square containing both a snake and a
                                                           // ladder.
    private static final Color NEUTRAL_COLOR = Color.RESET; // The color of a square containing nothing.

    private final SquareType LINKED_TYPE; // The type of the link to this square (snake, ladder, or none).
    private final int LINKED_SQUARE; // The square linked to this one (by a snake or a ladder).
    private final int NB; // The position of this square.
    private final Color COLOR; // The color of this square.

    private final ArrayList<Player> CURRENT_PLAYERS = new ArrayList<>(2);

    /* CONSTRUCTORS */

    /**
     * Default parameter-less constructor.
     */
    public Square() {
        this(-1, 0, SquareType.None);
    }

    /**
     * Default constructor.
     *
     * @param NB            The position of this square.
     * @param LINKED_TYPE   The type of the link to this square (snake, ladder, or
     *                      none).
     * @param LINKED_SQUARE The square linked to this one (by a snake or a ladder).
     */
    public Square(int LINKED_SQUARE, int NB, SquareType LINKED_TYPE) {
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

    /**
     * Gets the position of the linked square.
     *
     * @return The position of the linked square.
     */
    public int getLINKED_SQUARE() {
        return LINKED_SQUARE;
    }

    /**
     * Gets the position of this square.
     *
     * @return The position of this square.
     */
    public int getNB() {
        return NB;
    }

    /**
     * Gets the type of the link to this square (snake, ladder, or none).
     *
     * @return The type of the link to this square (snake, ladder, or none).
     */
    public SquareType getLINKED_TYPE() {
        return LINKED_TYPE;
    }

    /**
     * Gets the current players on this square.
     *
     * @return The current players on this square.
     */
    public ArrayList<Player> getCURRENT_PLAYERS() {
        return CURRENT_PLAYERS;
    }

    /**
     * Gets the color of this square.
     *
     * @return The color of this square.
     */
    public Color getCOLOR() {
        return COLOR;
    }

    /**
     * Gets the constant color of a square containing a snake.
     *
     * @return The constant color of a square containing a snake.
     */
    public static Color getSNAKE_COLOR() {
        return SNAKE_COLOR;
    }

    /**
     * Gets the constant color of a square containing a ladder.
     *
     * @return The constant color of a square containing a ladder.
     */
    public static Color getLADDER_COLOR() {
        return LADDER_COLOR;
    }

    /**
     * Gets the constant color of a square containing both a snake and a ladder.
     *
     * @return The constant color of a square containing both a snake and a ladder.
     */
    public static Color getMIXED_COLOR() {
        return MIXED_COLOR;
    }

    /**
     * Gets the constant color of a square containing nothing.
     *
     * @return The constant color of a square containing nothing.
     */
    public static Color getNEUTRAL_COLOR() {
        return NEUTRAL_COLOR;
    }

    /* PUBLIC METHODS */

    /**
     * Adds a player to the current players on this square.
     *
     * @param currentPlayer The player to add.
     */
    public void addCurrentPlayer(Player currentPlayer) {
        this.CURRENT_PLAYERS.add(currentPlayer);
    }

    /**
     * Removes a player from the current players on this square.
     *
     * @param currentPlayer The player to remove.
     */
    public void removeCurrentPlayer(Player currentPlayer) {
        this.CURRENT_PLAYERS.remove(currentPlayer);
    }

    /**
     * Resets this Square's state.
     */
    public void reset() {
        CURRENT_PLAYERS.clear();
    }
}
