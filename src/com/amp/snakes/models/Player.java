// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 7th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes.models;

import com.amp.snakes.enums.Color;

/**
 * Represents a player.
 */
public class Player implements Comparable<Player> {
    private final String NAME; // The player's name.
    private final Color COLOR; // The player's color.

    private int lastRoll = -1; // The last roll of the player.
    private int position = 0; // The current position of the player.

    /* CONSTRUCTORS */

    /**
     * Default constructor.
     * 
     * @param NAME  The player's name.
     * @param COLOR The player's color.
     */
    public Player(String NAME, Color COLOR) {
        this.NAME = NAME;
        this.COLOR = COLOR;
    }

    /* ACCESSORS & MUTATORS */

    /**
     * Gets the player's name.
     * 
     * @return The player's name.
     */
    public String getNAME() {
        return COLOR.getValBold() + NAME + Color.RESET.getValBold();
    }

    /**
     * Gets the player's short name (max 2 letters).
     * 
     * @return The player's short name.
     */
    public String getShortName() {
        if (NAME.length() >= 2)
            return COLOR.getValBold() + NAME.substring(0, 2) + Color.RESET.getValBold();
        else if (NAME.length() == 0)
            return "  ";
        else
            return COLOR.getValBold() + (NAME + " ") + Color.RESET.getValBold();
    }

    /**
     * Gets the player's last roll.
     * 
     * @return The player's last roll.
     */
    public int getLastRoll() {
        return lastRoll;
    }

    /**
     * Sets the player's last roll.
     * 
     * @param lastRoll The player's last roll.
     */
    public void setLastRoll(int lastRoll) {
        this.lastRoll = lastRoll;
    }

    /**
     * Gets the player's 1-based position.
     * 
     * @return The player's 1-based position.
     */
    public int getPosition() {
        return Math.max(position, 1);
    }

    /**
     * Sets the player's position.
     * 
     * @param position The player's position.
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Advances the player's position by the increment.
     * 
     * @param increment The increment.
     */
    public void advancePosition(int increment) {
        this.position += increment;
    }

    /* PUBLIC METHODS */

    /**
     * Reset the player's state.
     */
    public void reset() {
        lastRoll = -1;
        position = 0;
    }

    /* OVERRIDES */

    /**
     * Compares this player to another player.
     * 
     * @param other The other player.
     * @return The comparison result.
     */
    @Override
    public int compareTo(Player o) {
        return Integer.compare(o.getLastRoll(), getLastRoll());
    }
}
