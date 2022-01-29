package com.amp.snakes.models;

import com.amp.snakes.enums.Color;

public class Player implements Comparable<Player> {
    private final String NAME;
    private final Color COLOR;

    private int lastRoll = -1;
    private int position = 0;

    /* CONSTRUCTORS */

    public Player(String NAME, Color COLOR) {
        this.NAME = NAME;
        this.COLOR = COLOR;
    }

    /* ACCESSORS & MUTATORS */

    public String getNAME() {
        return COLOR.getValNorm() + NAME + Color.RESET.getValNorm();
    }
    public String getShortName(){
        if (NAME.length() >= 2) return COLOR.getValNorm() + NAME.substring(0,2) + Color.RESET.getValNorm();
        else return COLOR.getValNorm() + (NAME + " ") + Color.RESET.getValNorm();
    }

    public int getLastRoll() {
        return lastRoll;
    }

    public void setLastRoll(int lastRoll) {
        this.lastRoll = lastRoll;
    }

    public int getPosition() {
        // since position is not 0-based, position can never be 0 (as the array index would be -1)
        return Math.max(position, 1); //todo faire un if pour s'assurer que c'est pas un 0 quand on appelle cette methode au lieu de faire un max ici
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void advancePosition(int increment) {
        this.position += increment;
    }

    /* PUBLIC METHODS */

    public void reset(){
        lastRoll = -1;
        position = 0;
    }

    /* OVERRIDES */

    @Override
    public int compareTo(Player o) {
        return Integer.compare(o.getLastRoll(), getLastRoll());
    }
}
