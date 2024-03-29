// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 7th
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

import java.util.ArrayList;
import java.util.Random;

import static com.amp.snakes.utility.Globals.SYSTEM_SCANNER;

/**
 * Represents a game of LadderAndSnake.
 */
public class LadderAndSnake {
    private final Square[][] GAME_BOARD; // 2D Array for the game board (always a square board)
    private final int BOARD_SIZE; // the size of a side of the game board
    private final int NB_PLAYERS; // number of players currently playing the game

    private Player[] players; // array containing all the current players (in playing order)

    private boolean hasGameFinished; // do we have a winner?

    /* CONSTRUCTORS */

    /**
     * Constructor for the class
     *
     * @param players the ordered list of players
     */
    public LadderAndSnake(Player[] players) {
        this(ConfigHandler.getBoardConfig(null), players);
    }

    /**
     * Default constructor
     *
     * @param GAME_BOARD the custom game board (if null, the game exits)
     * @param players    the ordered list of players
     */
    private LadderAndSnake(Square[][] GAME_BOARD, Player[] players) {
        if (GAME_BOARD == null) {
            this.BOARD_SIZE = -1;
            this.NB_PLAYERS = -1;
            this.GAME_BOARD = null;

            System.out.println("Error: Invalid Board Config File! Current game is borked and will close...");
            SYSTEM_SCANNER.close();
            System.exit(-1);
            return;
        }

        this.players = players.clone();

        this.BOARD_SIZE = GAME_BOARD.length;
        this.NB_PLAYERS = players.length;
        this.GAME_BOARD = GAME_BOARD.clone();
    }

    /* ACCESSORS & MUTATORS */

    /**
     * Getter for the game board
     *
     * @return the game board
     */
    public Square[][] getGAME_BOARD() {
        return GAME_BOARD;
    }

    /**
     * Getter for the size of the game board
     *
     * @return the size of the game board
     */
    public int getBOARD_SIZE() {
        return BOARD_SIZE;
    }

    /**
     * Getter for the number of players
     *
     * @return the number of players
     */
    public int getNB_PLAYERS() {
        return NB_PLAYERS;
    }

    /**
     * Getter for the players
     *
     * @return the players array
     */
    public Player[] getPlayers() {
        return players;
    }

    /* PUBLIC METHODS */

    /**
     * Starts and plays the game
     */
    public void play() {
        while (!hasGameFinished) {
            System.out.println("Press [ENTER] to continue...");

            SYSTEM_SCANNER.nextLine();

            playOneTurn();
        }
    }

    /**
     * Resets the game (for a rerun)
     */
    public void reset() {
        for (Player player : players)
            player.reset();

        for (int i = 0; i < BOARD_SIZE * BOARD_SIZE; i++)
            GAME_BOARD[i / BOARD_SIZE][i % BOARD_SIZE].reset();

        hasGameFinished = false;
    }

    /**
     * Flips the dice for the specified player and prints the result
     *
     * @param index   the player to flip the dice for
     * @param players the players array we're getting the player from
     */
    public void playerFlipDice(int index, Player[] players) {
        players[index].setLastRoll(flipDice());
        System.out.println(players[index].getNAME() + " rolled a " + players[index].getLastRoll() + "!");
    }

    /* PRIVATE METHODS */

    /**
     * Prints the game board to the console
     */
    private void printBoard() {
        int width = 12;
        int height = 4;

        // Skip some iterations of the loop when we're printing inside a square
        // For eg, if we display 100 (3 pixels), the loop will only go forward by 1
        // pixel per iteration, so we have to skip 2 iterations so the loop catches up
        // with where the next character can be displayed
        int skipIterations = 0;

        for (int row = BOARD_SIZE * height; row >= 0; row--) {
            for (int column = 0; column <= BOARD_SIZE * width; column++) {

                System.out.print(squareColor(row, column, width, height).getValNorm());

                if (row == BOARD_SIZE * height) { // Top line

                    if (column == 0)
                        System.out.print("╔"); // Left column
                    else if (column == BOARD_SIZE * width)
                        System.out.print("╗"); // Right column
                    else if (column % width == 0)
                        System.out.print("╤"); // Middle separator
                    else
                        System.out.print("═"); // Inside a square

                } else if (row == 0) { // Bottom line

                    if (column == 0)
                        System.out.print("╚"); // Left column
                    else if (column == BOARD_SIZE * width)
                        System.out.print("╝"); // Right column
                    else if (column % width == 0)
                        System.out.print("╧"); // Middle separator
                    else
                        System.out.print("═"); // Inside a square

                } else if (row % height == 0) { // Horizontal separator

                    if (column == 0)
                        System.out.print("╟"); // Left column
                    else if (column == BOARD_SIZE * width)
                        System.out.print("╢"); // Right column
                    else if (column % width == 0)
                        System.out.print("┼"); // Middle separator
                    else
                        System.out.print("─"); // Inside a square

                } else { // Inside a square

                    if (column == 0 || column == BOARD_SIZE * width) { // Left or right column
                        System.out.print("║");
                    } else if (column % width == 0) { // Middle separator
                        System.out.print("│");
                    } else { // Information inside a square

                        int boardRow = row / height;

                        // if it's an odd-numbered row, flip it to match a real board configuration
                        int adjustedBoardColumn = boardRow % 2 != 0 ? BOARD_SIZE - 1 - column / width : column / width;

                        // Bottom left corner of a square, display square number
                        if ((row - 1) % height == 0 && (column - 1) % width == 0) {
                            System.out.print(GAME_BOARD[boardRow][adjustedBoardColumn].getCOLOR().getValBold()
                                    + String.format("%1$-3d", GAME_BOARD[boardRow][adjustedBoardColumn].getNB())
                                    + Color.RESET.getValBold());
                            skipIterations = 2;

                        }
                        // middle left corner of a square, display all players on the current square
                        else if ((row + 2) % height == 0 && (column - 1) % width == 0) {
                            ArrayList<Player> squarePlayers = GAME_BOARD[boardRow][adjustedBoardColumn].getCURRENT_PLAYERS();

                            if (!squarePlayers.isEmpty()) {
                                for (int i = 0; i < squarePlayers.size(); i++) {
                                    // Printing fourth player out of 4
                                    if (i == squarePlayers.size() - 1 && squarePlayers.size() >= 4) {
                                        System.out.print(squarePlayers.get(i).getShortName());
                                        skipIterations += 2;
                                    } else {
                                        System.out.print(squarePlayers.get(i).getShortName() + " ");
                                        if (i == 0)
                                            skipIterations += 2; // First player
                                        else
                                            skipIterations += 3;
                                    }
                                }
                            } else {
                                skipIterations += 2;
                                System.out.print("   ");
                            }

                        }
                        // top left corner of a square, display where snake or ladder goes (if there's one of them)
                        else if ((row + 1) % height == 0 && (column - 1) % width == 0) {
                            System.out.print(GAME_BOARD[boardRow][adjustedBoardColumn].getCOLOR().getValNorm());
                            switch (GAME_BOARD[boardRow][adjustedBoardColumn].getLINKED_TYPE().toString()) {
                                case ("None") -> System.out.print("           ");
                                case ("Snake") -> System.out.print("DOWN TO " + String.format("%1$-3d",
                                        GAME_BOARD[boardRow][adjustedBoardColumn].getLINKED_SQUARE()));
                                case ("Ladder") -> System.out
                                        .print("UP TO "
                                                + String.format("%1$-3d",
                                                GAME_BOARD[boardRow][adjustedBoardColumn].getLINKED_SQUARE())
                                                + "  ");
                            }
                            System.out.print(Color.RESET.getValNorm());
                            skipIterations = 10;
                        } else if (skipIterations > 0) {
                            skipIterations--;
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

    /**
     * Returns the color of the square at the specified row and column
     *
     * @param row    the row of the square
     * @param column the column of the square
     * @param width  the width of the board
     * @param height the height of the board
     * @return the color of the square
     */
    private Color squareColor(int row, int column, int width, int height) {

        boolean isVerticalEdge = column % width == 0;
        boolean isHorizEdge = row % height == 0;

        if (isVerticalEdge && isHorizEdge)
            return Square.getNEUTRAL_COLOR(); // Corner

        int boardRow = row / height;
        if (boardRow >= BOARD_SIZE)
            boardRow--; // Top row of pixels

        boolean isInverted = boardRow % 2 != 0; // True if column goes from right to left

        // gets the corrected column
        int boardColumn = isInverted ? BOARD_SIZE - column / width - 1 : column / width;

        if (boardColumn >= BOARD_SIZE)
            boardColumn--; // Right column
        if (boardColumn < 0)
            boardColumn++; // Left column

        // True if snake square nearby, false if not
        boolean snakeNearby = GAME_BOARD[boardRow][boardColumn].getLINKED_TYPE().equals(SquareType.Snake);

        // True if ladder square nearby, false if not
        boolean ladderNearby = GAME_BOARD[boardRow][boardColumn].getLINKED_TYPE().equals(SquareType.Ladder);

        // Check the edges
        if (isVerticalEdge && column != BOARD_SIZE * width) { // Check the right edge of a square
            if (!isInverted && boardColumn > 0)
                snakeNearby |= GAME_BOARD[boardRow][boardColumn - 1].getLINKED_TYPE().equals(SquareType.Snake);
            else if (isInverted && boardColumn < BOARD_SIZE - 1)
                snakeNearby |= GAME_BOARD[boardRow][boardColumn + 1].getLINKED_TYPE().equals(SquareType.Snake);

            if (!isInverted && boardColumn > 0)
                ladderNearby |= GAME_BOARD[boardRow][boardColumn - 1].getLINKED_TYPE().equals(SquareType.Ladder);
            else if (isInverted && boardColumn < BOARD_SIZE - 1)
                ladderNearby |= GAME_BOARD[boardRow][boardColumn + 1].getLINKED_TYPE().equals(SquareType.Ladder);
        }

        if (isHorizEdge) { // Check the top edge of a square
            // Inverted column (since the row above goes in the opposite direction)
            int otherColumn = isInverted ? column / width : BOARD_SIZE - column / width - 1;

            if (otherColumn >= BOARD_SIZE)
                otherColumn--;
            if (otherColumn < 0)
                otherColumn++;

            if (boardRow > 0)
                snakeNearby |= GAME_BOARD[boardRow - 1][otherColumn].getLINKED_TYPE().equals(SquareType.Snake);

            if (boardRow > 0)
                ladderNearby |= GAME_BOARD[boardRow - 1][otherColumn].getLINKED_TYPE().equals(SquareType.Ladder);
        }

        if (snakeNearby && ladderNearby)
            return Square.getMIXED_COLOR();
        else if (snakeNearby)
            return Square.getSNAKE_COLOR();
        else if (ladderNearby)
            return Square.getLADDER_COLOR();
        else
            return Square.getNEUTRAL_COLOR();
    }

    /**
     * Plays one turn for all players
     */
    private void playOneTurn() {
        for (int i = 0; i < NB_PLAYERS; i++) {
            if (hasGameFinished)
                break;

            playerFlipDice(i, players);
            playerMove(i);
        }
    }

    /**
     * Moves the specified player according to their last roll
     *
     * @param index the index of the player to play
     */
    private void playerMove(int index) {
        // removes the player from its old square
        GAME_BOARD[(players[index].getPosition() - 1) / BOARD_SIZE][(players[index].getPosition() - 1) % BOARD_SIZE]
                .removeCurrentPlayer(players[index]);

        players[index].advancePosition(players[index].getLastRoll());

        if (players[index].getPosition() > BOARD_SIZE * BOARD_SIZE) {
            players[index].setPosition(2 * BOARD_SIZE * BOARD_SIZE - players[index].getPosition());
        }

        // puts the player on its new square
        Square playerSquare = GAME_BOARD[(players[index].getPosition() - 1) / BOARD_SIZE][(players[index].getPosition() - 1) % BOARD_SIZE];
        playerSquare.addCurrentPlayer(players[index]);

        printBoard();

        if (players[index].getPosition() == BOARD_SIZE * BOARD_SIZE) {
            playerWon(index);
            return;
        }

        System.out.println();

        if (playerSquare.getLINKED_SQUARE() != -1) {
            System.out.println(playerSquare.getLINKED_TYPE() == SquareType.Snake
                    ? "Uh oh... Down " + players[index].getNAME() + " goes..."
                    : "Hurray! Up " + players[index].getNAME() + " goes!");

            // removes the player from its old square
            Square linkedSquare = GAME_BOARD[(playerSquare.getLINKED_SQUARE() - 1)
                    / BOARD_SIZE][(playerSquare.getLINKED_SQUARE() - 1) % BOARD_SIZE];
            playerSquare.removeCurrentPlayer(players[index]);

            players[index].setPosition(playerSquare.getLINKED_SQUARE());

            // puts the player on its new square
            linkedSquare.addCurrentPlayer(players[index]);

            printBoard();

            if (players[index].getPosition() == BOARD_SIZE * BOARD_SIZE) {
                playerWon(index);
                return;
            }

            System.out.println();
        }
    }

    /**
     * Prints the winning message to the console
     *
     * @param index index of the winning player
     */
    private void playerWon(int index) {
        System.out.println("CONGRATS! " + players[index].getNAME() + " has won this round!");

        System.out.print("Good luck to ");

        for (int i = 0; i < players.length; i++) {
            String suffix; //to correctly write the players wish message (comma & and)

            //most players will have a comma in-between their names
            if (i < players.length - 2) suffix = ", ";
            //"and" goes in fron of the before-last and last player's names
            else if (i == players.length - 2) suffix = " and ";
            else suffix = " ";

            if (i != index)
                System.out.print(players[i].getNAME() + suffix);
        }

        System.out.println("next time!");
        System.out.println();

        hasGameFinished = true;
    }

    /**
     * "Flips" a 6-sided dice
     *
     * @return a int between 1 and 6
     */
    private int flipDice() {
        return (new Random()).nextInt(6) + 1;
    }
}