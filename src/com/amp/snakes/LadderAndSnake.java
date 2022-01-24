// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes;

import com.amp.snakes.enums.SquareType;
import com.amp.snakes.models.Player;
import com.amp.snakes.models.Square;
import com.amp.snakes.utility.ConfigHandler;

import java.util.*;

public class LadderAndSnake {
    private final Square[][] GAME_BOARD;
    private final int BOARD_SIZE;
    private final int NB_PLAYERS;

    private Player[] players;
    private boolean hasGameStarted;
    private boolean hasGameFinished;

    /* CONSTRUCTORS */

    public LadderAndSnake(int NB_PLAYERS) {
        this(ConfigHandler.getBoardConfig(null), NB_PLAYERS);
    }

    public LadderAndSnake(LadderAndSnake ladderAndSnake) {
        this(ladderAndSnake.GAME_BOARD, ladderAndSnake.NB_PLAYERS);
    }

    private LadderAndSnake(Square[][] GAME_BOARD, int NB_PLAYERS) {
        if (GAME_BOARD == null) {
            this.BOARD_SIZE = -1;
            this.NB_PLAYERS = -1;
            this.GAME_BOARD = null;

            System.out.println("Error: Invalid Board Config File! Current game is borked and will close...");
            System.exit(-1);
            return;
        }

        this.BOARD_SIZE = GAME_BOARD.length;
        this.NB_PLAYERS = NB_PLAYERS;
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

    /* PUBLIC METHODS */

    public void play() {
        if (!hasGameStarted) firstTimeSetup();

        playOneTurn();
    }

    public void printBoard() {
        int width = 6;
        int height = 4;
        int skip = 0;
        for (int row = BOARD_SIZE * height; row >= 0; row--) {
            for (int column = 0; column <= BOARD_SIZE * width; column++) {

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
                        int adjustedBoardColumn = boardRow % 2 != 0 ? BOARD_SIZE - 1 - column / width : column / width;
                        if ((row - 1) % height == 0 && (column - 1) % width == 0) { //Bottom left corner of a square
                            //if it's an odd-numbered row, flip it to match a real board configuration
                            System.out.printf("%1$-3d", GAME_BOARD[boardRow][adjustedBoardColumn].getNB());
                            skip = 2;
                        } else if ((row + 2) % height == 0 && (column - 1) % width == 0) { //middle left corner of a square
                            //display all players on the current square
                            ArrayList<Player> squarePlayers = GAME_BOARD[row / height][adjustedBoardColumn].getCurrentPlayers();

                            if (!squarePlayers.isEmpty())
                                for (Player player : squarePlayers) {
                                    System.out.printf("%-" + (4 / squarePlayers.size()) + "s", player.getNAME());
                                }
                            else
                                System.out.print("    ");
                            skip = 3;
                        } else if ((row + 1) % height == 0 && (column - 1) % width == 0) { //top left corner of a square
                            switch (GAME_BOARD[row / height][column / width].getLINKED_TYPE().toString()) {
                                case ("None") -> System.out.print("     ");
                                case ("Snake") -> System.out.print("s>" + String.format("%1$-3d", GAME_BOARD[row / height][column / width].getLINKED_SQUARE()));
                                case ("Ladder") -> System.out.print("l>" + String.format("%1$-3d", GAME_BOARD[row / height][column / width].getLINKED_SQUARE()));
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

    /* PRIVATE METHODS */

    private void firstTimeSetup() {
        System.out.println("Game is played by " + NB_PLAYERS + " players.");

        System.out.println();

        players = new Player[NB_PLAYERS];

        System.out.println("Please name yourselves..."); //TODO: ask for choice of color too!

        Scanner playerName = new Scanner(System.in);

        for (int i = 0; i < NB_PLAYERS; i++) {
            System.out.print("Name for Player " + (i + 1) + ": ");
            players[i] = new Player(playerName.nextLine());
        }

        playerName.close();

        System.out.println("Thank you!");

        System.out.println();

        //TODO: manually resolve ties!

        System.out.println("Now deciding which player is starting:");

        for (int i = 0; i < NB_PLAYERS; i++) {
            playerFlipDice(i);
        }

        Arrays.sort(players);

        System.out.println();

        System.out.println("Playing order is: ");

        for (int i = 0; i < NB_PLAYERS; i++) {
            System.out.println((i + 1) + ". " + players[i].getNAME());
        }

        System.out.println();

        hasGameStarted = true;
    }

    private void playOneTurn() {
        for (int i = 0; i < NB_PLAYERS; i++) {
            playerFlipDice(i);
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

    private void playerFlipDice(int index) {
        players[index].setLastRoll(flipDice());
        System.out.println(players[index].getNAME() + " rolled a " + players[index].getLastRoll() + "!");
    }

    private int flipDice() {
        return (new Random()).nextInt(5) + 1;
    }
}