// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes;

import com.amp.snakes.enums.Color;
import com.amp.snakes.enums.SquareType;
import com.amp.snakes.models.Player;
import com.amp.snakes.models.Square;
import com.amp.snakes.utility.ConfigHandler;

import java.util.*;

public class LadderAndSnake {
    private final Square[][] GAME_BOARD;
    private final int BOARD_SIZE;
    private final int NB_PLAYERS;

    private static Player[] players;

    private boolean hasGameFinished;

    /* CONSTRUCTORS */

    public LadderAndSnake(Player[] players) {
        this(ConfigHandler.getBoardConfig(null), players);
    }

    public LadderAndSnake(LadderAndSnake ladderAndSnake) {
        this(ladderAndSnake.GAME_BOARD, getPlayers());
    }

    private LadderAndSnake(Square[][] GAME_BOARD, Player[] players) {
        if (GAME_BOARD == null) {
            this.BOARD_SIZE = -1;
            this.NB_PLAYERS = -1;
            this.GAME_BOARD = null;

            System.out.println("Error: Invalid Board Config File! Current game is borked and will close...");
            System.exit(-1);
            return;
        }

        LadderAndSnake.players = new Player[players.length];
        for (int i = 0; i < players.length; i++) {
            LadderAndSnake.players[i] = players[i];
        }
        this.BOARD_SIZE = GAME_BOARD.length;
        this.NB_PLAYERS = players.length;
        this.GAME_BOARD = Objects.requireNonNull(GAME_BOARD).clone();
    }

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

    public static Player[] getPlayers() {
        return players;
    }

    /* PUBLIC METHODS */

    public void play() {
        playOneTurn();
    }

    public static void playerFlipDice(int index, Player[] players) {
        players[index].setLastRoll(flipDice());
        System.out.println(players[index].getNAME() + " rolled a " + players[index].getLastRoll() + "!");
    }

    public void printBoard() {
        //╔═╤═╗
        //║ │ ║
        //╟─┼─╢
        //╚═╧═╝ todo remove this
        int width = 12; //todo this will be a private method (move it in the private methods group)
        int height = 4;
        int skip = 0;
        for (int row = BOARD_SIZE * height; row >= 0; row--) {
            for (int column = 0; column <= BOARD_SIZE * width; column++) {

                System.out.print(squareColor(row, column, width, height).getValNorm());

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

                        int boardRow = row / height;
                        int adjustedBoardColumn = boardRow % 2 != 0 ? BOARD_SIZE - 1 - column / width : column / width; //if it's an odd-numbered row, flip it to match a real board configuration

                        if ((row - 1) % height == 0 && (column - 1) % width == 0) { //Bottom left corner of a square

                            System.out.print(GAME_BOARD[boardRow][adjustedBoardColumn].getCOLOR().getValBold() + String.format("%1$-3d", GAME_BOARD[boardRow][adjustedBoardColumn].getNB()) + Color.RESET.getValBold());
                            skip = 2;

                        } else if ((row + 2) % height == 0 && (column - 1) % width == 0) { //middle left corner of a square
                            //display all players on the current square
                            ArrayList<Player> squarePlayers = GAME_BOARD[row / height][adjustedBoardColumn].getCurrentPlayers();

                            if (!squarePlayers.isEmpty()) {
                                for (Player player : squarePlayers) {
                                    System.out.print(player.getShortName() + " ");
                                    skip += player.getShortName().length() - 11;
                                }
                            } else {
                                skip += 2;
                                System.out.print("   ");
                            }

                        } else if ((row + 1) % height == 0 && (column - 1) % width == 0) { //top left corner of a square
                            System.out.print(GAME_BOARD[boardRow][adjustedBoardColumn].getCOLOR().getValBold());
                            switch (GAME_BOARD[boardRow][adjustedBoardColumn].getLINKED_TYPE().toString()) {
                                case ("None") -> System.out.print("           ");
                                case ("Snake") -> System.out.print("DOWN TO " + String.format("%1$-3d", GAME_BOARD[boardRow][adjustedBoardColumn].getLINKED_SQUARE()));
                                case ("Ladder") -> System.out.print("UP TO " + String.format("%1$-3d", GAME_BOARD[boardRow][adjustedBoardColumn].getLINKED_SQUARE()) + "  ");
                            }
                            System.out.print(Color.RESET.getValBold());
                            skip = 10;
                        } else if (skip > 0) {
                            skip--;
                        } else {
                            System.out.print(" ");
                        }

                    }

                }
                System.out.print(Square.getNEUTRAL_COLOR().getValNorm());
            }
            System.out.println();
        }
    }


    /* PRIVATE METHODS */

    private Color squareColor(int row, int column, int width, int height) {

        boolean isVerticalEdge = column % width == 0;
        boolean isHorizEdge = row % height == 0;

        if (isVerticalEdge && isHorizEdge) return Square.getNEUTRAL_COLOR(); //Corner

        int boardRow = row / height;
        if (boardRow >= BOARD_SIZE) boardRow--; //Top row of pixels

        boolean isInverted = boardRow % 2 != 0; //True if column goes from right to left
        int boardColumn = isInverted ? BOARD_SIZE - column / width - 1 : column / width;
        if (boardColumn >= BOARD_SIZE) boardColumn--; //Right column
        if (boardColumn < 0) boardColumn++; //Left column

        boolean snakeNearby = GAME_BOARD[boardRow][boardColumn].getLINKED_TYPE().toString().equals("Snake"); //True if snake square nearby, false if not
        boolean ladderNearby = GAME_BOARD[boardRow][boardColumn].getLINKED_TYPE().toString().equals("Ladder"); //True if ladder square nearby, false if not

        //Check the edges
        if (isVerticalEdge && column != BOARD_SIZE * width) { //Check the right edge of a square
            if (!isInverted && boardColumn > 0) snakeNearby |= GAME_BOARD[boardRow][boardColumn - 1].getLINKED_TYPE().toString().equals("Snake");
            else if (isInverted && boardColumn < BOARD_SIZE - 1) snakeNearby |= GAME_BOARD[boardRow][boardColumn + 1].getLINKED_TYPE().toString().equals("Snake");

            if (!isInverted && boardColumn > 0) ladderNearby |= GAME_BOARD[boardRow][boardColumn - 1].getLINKED_TYPE().toString().equals("Ladder");
            else if (isInverted && boardColumn < BOARD_SIZE - 1) ladderNearby |= GAME_BOARD[boardRow][boardColumn + 1].getLINKED_TYPE().toString().equals("Ladder");
        }

        if (isHorizEdge) { //Check the top edge of a square
            int otherColumn = isInverted ? column / width : BOARD_SIZE - column / width - 1; //Inverted column (since the row above goes in the opposite direction)
            if (otherColumn >= BOARD_SIZE) otherColumn--;
            if (otherColumn < 0) otherColumn++;

            if (boardRow > 0) snakeNearby |= GAME_BOARD[boardRow - 1][otherColumn].getLINKED_TYPE().toString().equals("Snake");

            if (boardRow > 0) ladderNearby |= GAME_BOARD[boardRow - 1][otherColumn].getLINKED_TYPE().toString().equals("Ladder");
        }

        if (snakeNearby && ladderNearby) return Square.getMIXED_COLOR();
        else if (snakeNearby) return Square.getSNAKE_COLOR();
        else if (ladderNearby) return Square.getLADDER_COLOR();
        else return Square.getNEUTRAL_COLOR();
    }

    private void playOneTurn() {
        for (int i = 0; i < NB_PLAYERS; i++) {
            playerFlipDice(i, players);
            playerMove(i);
        }
    }

    private void playerMove(int index) {
        //removes the player from its old square
        GAME_BOARD[(players[index].getPosition() - 1) / BOARD_SIZE][(players[index].getPosition() - 1) % BOARD_SIZE].removeCurrentPlayer(players[index]);

        players[index].advancePosition(players[index].getLastRoll());

        //puts the player on its new square
        Square playerSquare = GAME_BOARD[(players[index].getPosition() - 1) / BOARD_SIZE][(players[index].getPosition() - 1) % BOARD_SIZE];
        playerSquare.addCurrentPlayer(players[index]);

        printBoard();

        System.out.println();

        if (playerSquare.getLINKED_SQUARE() != -1) {
            System.out.println(playerSquare.getLINKED_TYPE() == SquareType.Snake ? "Uh oh... Down " + players[index].getNAME() + " goes..." : "Hurray! Up " + players[index].getNAME() + " goes!");

            //removes the player from its old square
            Square linkedSquare = GAME_BOARD[(playerSquare.getLINKED_SQUARE() - 1) / BOARD_SIZE][(playerSquare.getLINKED_SQUARE() - 1) % BOARD_SIZE];
            playerSquare.removeCurrentPlayer(players[index]);

            players[index].setPosition(playerSquare.getLINKED_SQUARE());

            //puts the player on its new square
            linkedSquare.addCurrentPlayer(players[index]);

            printBoard();

            System.out.println();
        }
    }

    private static int flipDice() {
        return (new Random()).nextInt(2) + 1;
    }
}