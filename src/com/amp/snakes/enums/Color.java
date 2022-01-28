package com.amp.snakes.enums;

import java.util.ArrayList;

public enum Color {

    RESET("\033[0m", "\033[0m", "\033[0m"),

    RED("\033[0;31m", "\033[1;31m", "\033[4;31m"),
    GREEN("\033[0;32m", "\033[1;32m", "\033[4;32m"),
    YELLOW("\033[0;33m", "\033[1;33m", "\033[4;33m"),
    BLUE("\033[0;34m", "\033[1;34m", "\033[4;34m"),
    PURPLE("\033[0;35m", "\033[1;35m", "\033[4;35m"),
    CYAN("\033[0;36m", "\033[1;36m", "\033[4;36m"),

    BLACK_BOLD(null, "\033[1;30m", null); //used in menus

    private final String valNorm;
    private final String valBold;
    private final String valUnder;

    private Color(String valNorm, String valBold, String valUnder) {
        this.valNorm = valNorm;
        this.valBold = valBold;
        this.valUnder = valUnder;
    }

    public String getValNorm() {
        return valNorm;
    }

    public String getValBold() {
        return valBold;
    }

    public String getValUnder() {
        return valUnder;
    }

    /**
     * To have an arraylist with the base colors (without reset or BLACK_BOLD)
     *
     * @return Arraylist with the base colors
     */
    public static ArrayList<Color> getBaseColors() {
        ArrayList<Color> colors = new ArrayList<Color>();
        colors.add(RED);
        colors.add(GREEN);
        colors.add(YELLOW);
        colors.add(BLUE);
        colors.add(PURPLE);
        colors.add(CYAN);
        return colors;
    }
}
