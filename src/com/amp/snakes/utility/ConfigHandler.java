// -----------------------------------------------------
// Assignment 1 - COMP 249
// Due Date: February 7th
// Question: Part I
// Written by: Augusto Mota Pinheiro (40208080)
//             Michaël Gugliandolo (40213419)
// -----------------------------------------------------

package com.amp.snakes.utility;

import com.amp.snakes.models.Square;
import com.amp.snakes.enums.SquareType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class is responsible for reading the configuration file and creating the
 * board.
 */
public class ConfigHandler {
    /**
     * Gets the board config at the specified path.
     * 
     * @param path The path to the config file.
     * @return The game board as a 2D array of Squares.
     */
    public static Square[][] getBoardConfig(String path) {
        Square[][] board = null; // board to be returned
        int boardSize = -1; // board size (to be read from config)

        String rawConfig; // raw config string
        Scanner rawConfigScanner; // scanner to read the config

        SquareType[] squareTypes = SquareType.values(); // all possible square types (to avoid fetching them every time)
        int index = 0; // index of the current line
        boolean onFirst = true; // whether the current line is the first one

        // default config path if null is given
        if (path == null)
            path = "board.conf";

        rawConfig = loadConfig(path);

        // if config is empty, return null (an error occurred while reading the file)
        if (rawConfig.isEmpty())
            return null;

        rawConfigScanner = new Scanner(rawConfig);

        while (rawConfigScanner.hasNextLine()) {
            // the first line should be the board size
            if (onFirst) {
                onFirst = false;
                boardSize = rawConfigScanner.nextInt();
                board = new Square[boardSize][boardSize];
                continue;
            }

            // if reading the boardSize failed, an error occurred
            if (boardSize == -1)
                return null;

            SquareType squareType = squareTypes[rawConfigScanner.nextInt()];
            int linkedSquare = rawConfigScanner.nextInt();
            // rawConfigScanner.nextLine();

            // Inspired from here:
            // https://stackoverflow.com/questions/19320183/1d-array-to-2d-array-mapping
            board[index / boardSize][index % boardSize] = new Square(linkedSquare, index + 1, squareType);
            index++;
        }

        rawConfigScanner.close();

        return board;
    }

    /**
     * Loads the config file at the specified path.
     * 
     * @return the config file as a parsed string
     */
    private static String loadConfig(String path) {
        try {
            File configFile = new File(path); // config file to be read

            Scanner configReader = new Scanner(configFile); // config file reader

            StringBuilder data = new StringBuilder(); // data to be returned

            boolean onFirst = true; // indicates if we're on the first line

            while (configReader.hasNextLine()) {
                String currLine = configReader.nextLine(); // current line

                // don't read comments or empty lines
                if (currLine.isEmpty() || currLine.charAt(0) == '#')
                    continue;

                // if we're on the first line, we don't need to add a newline
                if (onFirst) {
                    data.append(currLine);
                    onFirst = false;
                } else {
                    data.append('\n').append(currLine);
                }
            }

            configReader.close();

            return data.toString();
        } catch (FileNotFoundException e) {
            System.out.println("A FileNotFound error occurred when loading a config file at " + path + ".");
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            System.out.println("An error occurred when loading a config file at " + path + ".");
            e.printStackTrace();
            return "";
        }
    }
}
