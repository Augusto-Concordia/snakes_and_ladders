// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 7th
// Question: Part II
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes;

import com.amp.snakes.enums.Color;
import com.amp.snakes.models.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.amp.snakes.utility.Globals.SYSTEM_SCANNER;

/**
 * Driver class that creates players, starts the Ladder and Snake game, and asks to start over at the end
 */
public class PlayLadderAndSnake {

    public static void main(String[] args) {
        boolean playAgain = true; //True if user wants to play again, false if not (true by default to enter the while loop)

        LadderAndSnake game = null; //Ladder and Snake game object
        Player[] players = null; //Array of players

        System.out.println(Color.BLUE.getValBold() + "----------------------------");
        System.out.println("Welcome to Ladder and Snake!");
        System.out.println("----------------------------\n" + Color.RESET.getValBold());

        //Loop while user wants to continue playing
        while (playAgain) {
            boolean newPlayers = true; //True if creation of players is needed (ie first time playing or user wants to re-enter players at the end)
            if (players != null) { //Players exist, so not first time playing
                System.out.println("Do you wish to re-enter players? (Y/N)");
                String answer = SYSTEM_SCANNER.nextLine();
                newPlayers = answer.equals("Y");
            }

            if (newPlayers) { //Create players
                System.out.println("How many players are you? (Between 2 and 4 inclusively)");
                players = new Player[askNumberOfPlayers()];

                firstTimeSetup(players);

                game = new LadderAndSnake(players);
            } else { //Keep the same players
                game.reset();

                orderPlayers(players);
                System.out.println("Playing order is: ");
                for (int i = 0; i < players.length; i++)
                    System.out.println((i + 1) + ". " + players[i].getNAME());
            }

            game.play(); //Play game

            System.out.println("Do you wish to play again? (Y/N)"); //Ask to start over at the end
            String answer = SYSTEM_SCANNER.nextLine();

            playAgain = answer.equals("Y");
        }

        System.out.println("Thanks for playing!");

        SYSTEM_SCANNER.close(); //Close scanner
    }

    /**
     * Verify user's answer to the number of players and exit program after 4 failed attempts
     *
     * @return Number of players (between 2 and 4 inclusively)
     */
    private static int askNumberOfPlayers() {
        int attempts = 0; //Number of attempts
        String input; //Raw input from user
        int inputNb = 0; //Integer answer from user
        boolean error; //True if an exception was caught, false if answer is valid
        boolean isBobRoss = false; //True if user enters Bob Ross as the number of players, therefore removing the limit of 4 players

        do {
            error = false;

            try { //Try to convert user's input into an integer
                input = SYSTEM_SCANNER.nextLine();
                if (input.equals("Bob Ross")) {
                    isBobRoss = true;
                    System.out.println(Color.RED.getValBold() + "B" + Color.GREEN.getValBold() + "O" + Color.YELLOW.getValBold() + "B " + Color.BLUE.getValBold() +
                            "R" + Color.PURPLE.getValBold() + "O" + Color.CYAN.getValBold() + "S" + Color.RED.getValBold() + "S" + Color.GREEN.getValBold() + "!" + Color.RESET.getValBold());
                    System.out.println("The limit of 4 players has been removed! How many players are you? (2 or more)");
                    input = SYSTEM_SCANNER.nextLine();
                }
                inputNb = Integer.parseInt(input);
            } catch (Exception e) {
                error = true;
            }

            if (!error && !isBobRoss)
                error = (inputNb > 4 || inputNb < 2); //Look if between 2 and 4 (if previous try-catch caught an exception, then inputNb will keep its default value of 0 and this will stay false)
            else if (!error) //User has entered Bob Ross, so only check if >= 2
                error = inputNb < 2;

            if (error) { //There was an exception
                attempts++;

                System.out.print("Bad Attempt " + attempts + " - Invalid # of players or invalid input... ");

                if (attempts < 4) { //Less than 4 bad attempts, so user can try again
                    System.out.println("Please try again!");
                } else { //4 bad attempts, so exit program
                    System.out.println("Attempts exhausted: program will now exit!");
                    SYSTEM_SCANNER.close();
                    System.exit(0);
                }
            }
        } while (error);

        return inputNb;
    }

    /**
     * Create players (choose name and color)
     *
     * @param players Array of players already resized to the number of players
     */
    private static void firstTimeSetup(Player[] players) {
        ArrayList<Color> availableColors = Color.getBaseColors(); //Available colors

        System.out.println("Game is played by " + players.length + " players.");
        System.out.println();

        for (int i = 0; i < players.length; i++) //Create a player for each index in the array of players
            players[i] = createPlayer(i, availableColors, players.length > 4); //If there are more than 4 players, than isBobRoss is true

        System.out.println("Thank you!");
        System.out.println();

        System.out.println("Now deciding which player is starting:");

        orderPlayers(players); //Order players

        System.out.println();
        System.out.println("Playing order is: "); //Show playing order

        for (int i = 0; i < players.length; i++)
            System.out.println((i + 1) + ". " + players[i].getNAME());

        System.out.println();
    }

    /**
     * Create a new player (ie ask for name and color) and update the available colors
     *
     * @param step            Index of player in the array of players (for the 1st player, step = 0, 2nd player, step = 1, etc.)
     * @param availableColors ArrayList of the remaining colors to choose from (when a user picks a color, it's removed from the arraylist, unless isBobRoss is true)
     * @param isBobRoss       To know if the user entered the code to have a game with more than 4 players
     * @return New player
     */
    private static Player createPlayer(int step, ArrayList<Color> availableColors, boolean isBobRoss) {
        if (step == 0) //First time
            System.out.println("Bear in mind that only the first 2 characters of your names will be shown on the board!");

        System.out.print("Name for Player " + (step + 1) + ": ");
        String name = SYSTEM_SCANNER.nextLine();
        System.out.println();

        StringBuilder choices = new StringBuilder(); //String containing the remaining colors to choose from
        for (int i = 1; i <= availableColors.size(); i++)
            choices.append(availableColors.get(i - 1).getValNorm()).append("  [ ").append(i).append(" ]").append(Color.RESET.getValNorm());

        System.out.println(choices);
        System.out.print("Enter the number corresponding to the color you want : ");

        String answer; //User's answer
        Color color; //Color chosen
        boolean error = true; //True the answer isn't valid, false if it's valid
        int attempts = 0; //Number of attempts, exit the program after 4 bad attempts

        do {
            answer = SYSTEM_SCANNER.nextLine();
            try {
                if (Integer.parseInt(answer) < 1 || Integer.parseInt(answer) > availableColors.size()) { //Check if answer is between 1 and the number of colors
                    System.out.println("Number isn't between 1 and " + availableColors.size() + "!");
                } else { //No exception was caught and answer is between limits
                    error = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Answer isn't a number!");
            } catch (Exception e) {
                System.out.println("Exception other than NumberFormatException during validation of the choice of color.\n" + e.getMessage() + "\n");
            }

            if (error) { //There was an error
                attempts++;
                System.out.println("Bad Attempt " + attempts + "... Please try again!");
                if (attempts >= 4) { //4 bad attempts reached
                    System.out.println("Attempts exhausted: program will now exit!");
                    SYSTEM_SCANNER.close();
                    System.exit(0);
                }
            }
        } while (error); //Loop while there's an error with user's answer

        color = availableColors.get(Integer.parseInt(answer) - 1); //Get chosen color
        if (!isBobRoss) availableColors.remove(color); //Remove color from the available colors
        return new Player(name, color);
    }

    /**
     * Order all the players in an array with dice rolls (the largest number goes first, and tied players roll again)
     *
     * @param players Array of players to order
     */
    private static void orderPlayers(Player[] players) {
        //Roll a die for every player
        for (Player player : players) {
            player.setLastRoll((new Random()).nextInt(6) + 1);
            System.out.println(player.getNAME() + " rolled a " + player.getLastRoll() + "!");
        }
        System.out.println();

        Arrays.sort(players); //Sort players according to their dice roll

        //Deal with the ties

        boolean hasTie = false; //True if there is a tie, false if not
        boolean hasSurpassedTie = false; //True if there was a tie, but the number we're currently checking is different, so we passed all the numbers that were the same
        int beginTieIndex = 0; //Index of the first tied player in the array
        Player[] arrayTies; //Array with ONLY the tied players

        //Compare each number with the one behind it. If they are the same, but the next one is different, then isolate the tie in arrayTies and order it by calling this method again
        for (int i = 1; i < players.length; i++) {

            if (!hasTie && players[i].getLastRoll() == players[i - 1].getLastRoll()) { //Beginning of a tie, since hasTie is false
                hasTie = true;
                beginTieIndex = i - 1;
            }
            if (hasTie && players[i].getLastRoll() != players[i - 1].getLastRoll())
                hasSurpassedTie = true; //Was a tie, but the current number is different, so we're at the end of tie

            if (hasTie && (hasSurpassedTie || i == players.length - 1)) { //Check if we were in a tie, OR if we are in a tie and we're at the end of the loop

                if (hasSurpassedTie)
                    arrayTies = copyInNewArray(players, beginTieIndex, i - 1); //If the tie was surpassed, then stop copying the numbers at the previous one (since the current one is different)
                else
                    arrayTies = copyInNewArray(players, beginTieIndex, i); //We're at the end of the loop, so the current number is also tied

                System.out.println("We have a tie!");
                orderPlayers(arrayTies); //Order the tied players

                System.arraycopy(arrayTies, 0, players, beginTieIndex, arrayTies.length); //Copy the newly sorted players back into the main Player array

                //Reset variables
                hasTie = false;
                hasSurpassedTie = false;
            }
        }

    }

    /**
     * Copy players from a certain range from a source array into a new array
     *
     * @param players    Source array to copy from
     * @param beginIndex Index of first player to copy
     * @param endIndex   Index of last player to copy
     * @return New array filled with the copied players
     */
    private static Player[] copyInNewArray(Player[] players, int beginIndex, int endIndex) {

        Player[] newArray = new Player[endIndex - beginIndex + 1];
        System.arraycopy(players, beginIndex, newArray, 0, endIndex - beginIndex + 1);
        return newArray;
    }

}
