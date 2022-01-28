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
import java.util.Scanner;

public class PlayLadderAndSnake {

    public static void main(String[] args) {
        Scanner keyIn = new Scanner(System.in);
        System.out.println("Welcome! How many players are you? (Between 2 and 4 inclusively)");
        Player[] players = new Player[keyIn.nextInt()];

        firstTimeSetup(players);

        LadderAndSnake game = new LadderAndSnake(players);
        game.play();

        keyIn.close();
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
        Scanner keyIn = new Scanner(System.in);

        if (step == 0) System.out.println("Bear in mind that only the first 2 characters of your names will be shown on the board!");

        System.out.print("Name for Player " + (step + 1) + ": ");
        String name = keyIn.nextLine();

        System.out.println();

        StringBuilder choices = new StringBuilder();
        for (int i = 1; i <= availableColors.size(); i++)
            choices.append(availableColors.get(i - 1).getValNorm()).append("  [ ").append(i).append(" ]").append(Color.RESET.getValNorm());

        System.out.println(choices);
        System.out.print("Enter the number corresponding to the color you want : ");

        Color color = availableColors.get(keyIn.nextInt() - 1);
        availableColors.remove(color);

        keyIn.close();

        return new Player(name, color);
    }

    private static void orderPlayers(Player[] players) {
        for (int i = 0; i < players.length; i++)
            LadderAndSnake.playerFlipDice(i, players);

        System.out.println();
        Arrays.sort(players);

        boolean hasTie = false; //True if there is a tie
        ArrayList<Player> tiedPlayers = new ArrayList<>(); //All the players with the same dice roll
        Player[] arrayTies;

        for (int i = 1; i < players.length; i++) {
            if (players[i].getLastRoll() == players[i - 1].getLastRoll()) { //Tie
                hasTie = true;
                tiedPlayers.add(players[i - 1]);
            }

            //Reached a different dice roll, so deal with the tie from the previous players
            if (hasTie && (players[i].getLastRoll() != players[i - 1].getLastRoll() || i == players.length - 1)) {
                hasTie = false;

                if (i == players.length - 1) tiedPlayers.add(players[i]);
                else tiedPlayers.add(players[i - 1]);

                arrayTies = new Player[tiedPlayers.size()];
                tiedPlayers.toArray(arrayTies);
                tiedPlayers.clear();

                System.out.println("We have a tie!");

                orderPlayers(arrayTies);

                for (int j = i - arrayTies.length; j < i; j++) {
                    if (i == players.length - 1) players[j + 1] = arrayTies[j - i + arrayTies.length];
                    else players[j] = arrayTies[j - i + arrayTies.length];
                }
            }
        }

    }

}
