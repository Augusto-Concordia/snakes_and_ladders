// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 4th
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

public class PlayLadderAndSnake {
    public static void main(String[] args) {
        boolean playAgain = true;

        LadderAndSnake game = null;
        Player[] players = null;

        while (playAgain) {
            boolean newPlayers = true;
            if (players != null) {
                System.out.println("Do you wish to re-enter players? (Y/N)");
                String answer = SYSTEM_SCANNER.nextLine();
                newPlayers = answer.equals("Y");
            }

            if (newPlayers) {
                System.out.println("Welcome! How many players are you? (Between 2 and 4 inclusively)");
                players = new Player[askNumberOfPlayers()];

                firstTimeSetup(players);

                game = new LadderAndSnake(players);
            } else {
                game.reset();

                orderPlayers(players);
            }

            game.play();

            System.out.println("Do you wish to play again? (Y/N)");
            SYSTEM_SCANNER.nextLine(); //throwaway
            String answer = SYSTEM_SCANNER.nextLine();

            playAgain = answer.equals("Y");
        }

        System.out.println("Thanks for playing!");

        SYSTEM_SCANNER.close();
    }

    private static int askNumberOfPlayers() {
        int input;
        int attempts = 0;

        do {
            input = SYSTEM_SCANNER.nextInt();

            if (input > 4 || input < 2) {
                attempts++;

                System.out.print("Bad Attempt " + attempts + " - Invalid # of players... ");

                if (attempts < 4) System.out.println("Please try again!");
                else {
                    System.out.println("Attempts exhausted: program will now exit!");
                    System.exit(0);
                }
            }

        } while (input > 4 || input < 2);
        return input;
    }

    private static void firstTimeSetup(Player[] players) {
        ArrayList<Color> availableColors = Color.getBaseColors(); //available colors

        System.out.println("Game is played by " + players.length + " players.");
        System.out.println();

        for (int i = 0; i < players.length; i++)
            players[i] = createPlayer(i, availableColors);

        System.out.println("Thank you!");
        System.out.println();

        System.out.println("Now deciding which player is starting:");

        orderPlayers(players);

        System.out.println();
        System.out.println("Playing order is: ");

        for (int i = 0; i < players.length; i++)
            System.out.println((i + 1) + ". " + players[i].getNAME());

        System.out.println();
    }

    private static Player createPlayer(int step, ArrayList<Color> availableColors) {
        if (step == 0)
            System.out.println("Bear in mind that only the first 2 characters of your names will be shown on the board!");

        SYSTEM_SCANNER.nextLine();

        System.out.print("Name for Player " + (step + 1) + ": ");
        String name = SYSTEM_SCANNER.nextLine();

        System.out.println();

        StringBuilder choices = new StringBuilder();
        for (int i = 1; i <= availableColors.size(); i++)
            choices.append(availableColors.get(i - 1).getValNorm()).append("  [ ").append(i).append(" ]").append(Color.RESET.getValNorm());

        System.out.println(choices);
        System.out.print("Enter the number corresponding to the color you want : ");

        String answer = SYSTEM_SCANNER.nextLine();
        Color color;
        while (true) {
            try {
                if (Integer.parseInt(answer) < 1 || Integer.parseInt(answer) > availableColors.size()) {
                    System.out.println("Please enter a number between 1 and " + availableColors.size() + "!");
                } else {
                    color = availableColors.get(Integer.parseInt(answer) - 1);
                    availableColors.remove(color);
                    return new Player(name, color);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number!");
            } catch (Exception e) {
                System.out.println("Exception other than NumberFormatException during validation of the choice of color.\n\n" + e.getMessage());
            }
            answer = SYSTEM_SCANNER.nextLine();
        }
    }

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
                    arrayTies = copyNumsInNewArray(players, beginTieIndex, i - 1); //If the tie was surpassed, then stop copying the numbers at the previous one (since the current one is different)
                else
                    arrayTies = copyNumsInNewArray(players, beginTieIndex, i); //We're at the end of the loop, so the current number is also tied

                System.out.println("We have a tie!");
                orderPlayers(arrayTies); //Order the tied players

                System.arraycopy(arrayTies, 0, players, beginTieIndex, arrayTies.length); //Copy the newly sorted players back into the main Player array

                //Reset variables
                hasTie = false;
                hasSurpassedTie = false;
            }
        }

    }

    private static Player[] copyNumsInNewArray(Player[] players, int beginIndex, int endIndex) {

        Player[] newArray = new Player[endIndex - beginIndex + 1];
        System.arraycopy(players, beginIndex, newArray, 0, endIndex - beginIndex + 1);
        return newArray;
    }

}
